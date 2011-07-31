import Data.Char

-- Ex1 - credit card validation

digit = fromIntegral . digitToInt

toDigits :: Integer -> [Integer]
toDigits 0 = []
toDigits n      
    | n < 0 = []
    | otherwise = map digit (show n)

toDigitsRev = reverse . toDigits

double :: Integer -> Integer
double n = n * 2

doubleEveryOtherFromLeft :: [Integer] -> [Integer]
doubleEveryOtherFromLeft [] = []
doubleEveryOtherFromLeft (x:[]) = [x]
doubleEveryOtherFromLeft (x:y:xs) = x:(double y):(doubleEveryOtherFromLeft xs)

doubleEveryOther :: [Integer] -> [Integer]
doubleEveryOther xs = reverse (doubleEveryOtherFromLeft (reverse xs))


doubleEveryOtherDigits = doubleEveryOther . toDigits

sumDigits :: [Integer] -> Integer
sumDigits xs = (sum . concat) (map toDigits xs)

mod10 = flip mod 10
eq0 = (==) 0


validate = eq0 . mod10 . sumDigits . doubleEveryOther . toDigits 

-- Ex 2 hanoi

type Peg = String
type Move = (Peg, Peg)


hanoi :: Integer -> Peg -> Peg -> Peg -> [Move]
hanoi 0 _ _ _ = []
hanoi n a b c = (hanoi (n-1) a c b) ++ ((a,b) : hanoi (n-1) c b a)

main = do {
    putStrLn (show (validate 4012888888881881));
    putStrLn (show (hanoi 2 "a" "b" "c"));
}

