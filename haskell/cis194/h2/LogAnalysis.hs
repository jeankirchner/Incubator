{-# OPTIONS_GHC -Wall #-}

module LogAnalysis where

import Log

ts x = read x::TimeStamp
num x = read x::Int

parseIt :: Char -> [String] -> LogMessage
parseIt 'I' (t : xs) = LogMessage Info (ts t) (unwords xs)
parseIt 'W' (t : xs) = LogMessage Warning (ts t) (unwords xs)
parseIt 'E' (s : t : xs) = LogMessage (Error (num s)) (ts t) (unwords xs)

parseMessage :: String -> LogMessage
parseMessage (t : str) = parseIt t (words str)

parseAll :: [String] -> [LogMessage]
parseAll = map parseMessage

parse :: String -> [LogMessage]
parse = parseAll . lines

getTs :: LogMessage -> TimeStamp
getTs (Unknown s) = error s
getTs (LogMessage _ x _) = x 

insert :: LogMessage -> MessageTree -> MessageTree
insert (Unknown _) t = t
insert lm Leaf = Node Leaf lm Leaf
insert lm (Node l lm2 r)
    | (getTs lm) < (getTs lm2) = Node (insert lm l) lm2 r
    | otherwise = Node l lm2 (insert lm r)

build :: [LogMessage] -> MessageTree
build = foldl (flip insert) Leaf

inOrder :: MessageTree -> [LogMessage]
inOrder Leaf = []
inOrder (Node l lm r) = (inOrder l) ++ [lm] ++ (inOrder r)

msg (LogMessage _ _ m) = m


filterLogs :: LogMessage -> Bool
filterLogs (LogMessage (Error s) _ _) = s > 50
filterLogs _ = False


whatWentWrong :: [LogMessage] -> [String]
whatWentWrong = (map msg) . (filter filterLogs)
