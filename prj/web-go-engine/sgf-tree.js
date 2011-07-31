//@import go-def.js

    SGFGoNodePropertyValue = {
        letter: {},
        digit: {},
        none: {},
        number: {},
        real: {},
        double: {}, 
        color: {},
        simpleText: {},
        text: {},
        point: {},
        move: {},
        stone: {},
        compose: {}
        stoneList:{}
    }


    SGFExclusivinessControl = {
        SETUP: 0
    }

    /*
     * Human Readable Sgf Go node properties based on SGFGoRawNodeProperty
     */
    SGFGoNodeProperty = {

        //Move properties
        BlackMove: {},
        ExecuteMove: {},
        Numerate: {},
        WhiteMove: {},

        //Setup properties
        BlackStonesSetup: {},
        ClearPointsSetup: {},
        WhiteStonesSetup: {},
        TurnPlayerSetup: {},
        
        //Node annotation properties
        Comment: {},

        //TODO: Accordingly to sgf specification: {} this should display a message, review the name
        EvenPosition: {},
        
        //TODO: Accordingly to sgf specification: {} this should display a message, review the name
        BlackGoodPosition: {} ,

        //TODO: Accordingly to sgf specification: {} this should display a message, review the name
        WhiteGoodPosition: {},

        //TODO: Accordingly to sgf specification: {} this should display a message, review the name
        Hotspot: {},
        NameNode: {},
        
        //TODO: Accordingly to sgf specification: {} this should display a message, review the name
        UnclearPosition: {},
        EstimatedScore: {},
        
        //Move annotation properties
        BadMove: {},
        DoubtfulMove: {},
        InterestingMove: {},
        TesujiMove: {},

        //Markup properties
        ArrowPointToPoint: {},
        MarkPointCircle: {},
        FogStonesMark: {},
        LabelMark: {},
        LinePointToPoint: {},
        PlaceXs: {},
        SelectPoints: {},
        PlaceSquares: {},
        PlaceTriangles: {},

        //Root properties
        SetApplicationInfo: {},
        SetTextCharset: {},
        SetFileFormat: {},
        SetGameKind: {},
        SetVariationRenderType: {},
        SetBoardSize: {},

        //Game info properties
    	AnnotationsOwner: {},
        BlackRank: {},
        BlackTeamName: {},
        CopyrightInfo: {},
        GameDate: {} ,
        EventName: {} ,
        GameName: {},
        GeneralComments: {},
        OpeningStyle: {},
        ByoYomiMethod: {},
        BlackPlayerName: {},
        GamePlaceLocation: {},
        WhitePlayerName: {},
        GameResult: {},
        RoundNumberAndType: {},
        UsedRules: {},
        GameSource: {}, //book, journal
        TimeLimits: {},
        UserName: {},
        WhitePlayerRank: {},
        WhiteTeamName: {},

        //Timing properties
        BlackLeftTime: {},
        LeftBlackByoYomiMoves: {},
        LeftWhiteByoYomiMoves: {},
        WhiteLeftTime: {},

        //Miscellaneous properties
        BoardLayoutProperties: {},
        MovePrintingProperties: {},
        ViewPartOfBoard: {}
    };

    /*
     * Mapping of SGF Node properties with SGFGoNodeProperty: {} based on:
     * http://www.red-bean.com/sgf/properties.html
     */
    SGFGoNodePropertyKind = {

        //Move properties
        B: SGFGoNodePropety.BlackMove,
        KO: SGFGoNodeProperty.ExecuteMove,
        MN: SGFGoNodeProperty.NumerateMove,
        W: SGFGoNodeProperty.WhiteMove,

        //Setup properties
        AB: SGFGoNodeProperty.BlackStonesSetup,
        AE: SGFGoNodeProperty.ClearPointsSetup,
        AW: SGFGoNodeProperty.WhiteStonesSetup,
        PL: SGFGoNodeProperty.TurnPlayerSetup,

        //Node annotation properties
        C: SGFGoNodeProperty.Comment,
        DM: SGFGoNodeProperty.EvenPosition,
        GB: SGFGoNodeProperty.BlackGoodPosition,
        GW: SGFGoNodeProperty.WhiteGoodPosition,
        HO: SGFGoNodeProperty.Hotspot,
        N: SGFGoNodeProperty.NameNode,
        UC: SGFGoNodeProperty.UnclearPosition,
        V: SGFGoNodeProperty.EstimatedScore,

        //Move annotation properties
        BM: SGFGoNodeProperty.BadMove,
        DO: SGFGoNodeProperty.DoubtfulMove,
        IT: SGFGoNodeProperty.InterestingMove,
        TE: SGFGoNodeProperty.TesujiMove,

        //Markup properties
        AR: SGFGoNodeProperty.ArrowPointToPoint,
        CR: SGFGoNodeProperty.PlaceCircles,
        DD: SGFGoNodeProperty.FogStonesMark,
        LB: SGFGoNodeProperty.LabelMark,
        LN: SGFGoNodeProperty.LinePointToPoint,
        MA: SGFGoNodeProperty.PlaceXs,
        SL: SGFGoNodeProperty.SelectPoints,
        SQ: SGFGoNodeProperty.PlaceSquares,
        TR: SGFGoNodeProperty.PlaceTriangles,

        //Root properties
        AP: SGFGoNodeProperty.SetApplicationInfo,
        CA: SGFGoNodeProperty.SetTextCharset,
        FF: SGFGoNodeProperty.SetFileFormat,
        GM: SGFGoNodeProperty.SetGameKind,
        ST: SGFGoNodeProperty.SetVariationRenderType,
        SZ: SGFGoNodeProperty.SetBoardSize,

        //Game info properties
    	AN: SGFGoNodeProperty.AnnotationsOwner,
        BR: SGFGoNodeProperty.BlackRank,
        BT: SGFGoNodeProperty.BlackTeamName,
        CP: SGFGoNodeProperty.CopyrightInfo,
        DT: SGFGoNodeProperty.GameDate,
        EV: SGFGoNodeProperty.EventName,
        GN: SGFGoNodeProperty.GameName,
        GC: SGFGoNodeProperty.GeneralComments,
        ON: SGFGoNodeProperty.OpeningStyle,
        OT: SGFGoNodeProperty.ByoYomiMethod,
        PB: SGFGoNodeProperty.BlackPlayerName,
        PC: SGFGoNodeProperty.GamePlaceLocation,
        PW: SGFGoNodeProperty.WhitePlayerName,
        RE: SGFGoNodeProperty.GameResult,
        RO: SGFGoNodeProperty.RoundNumberAndType,
        RU: SGFGoNodeProperty.UsedRules,
        SO: SGFGoNodeProperty.GameSource, //book, journal
        TM: SGFGoNodeProperty.TimeLimits,
        US: SGFGoNodeProperty.UserName,
        WR: SGFGoNodeProperty.WhitePlayerRank,
        WT: SGFGoNodeProperty.WhiteTeamName,

        //Timing properties
        BL: SGFGoNodeProperty.BlackLeftTime,
        OB: SGFGoNodeProperty.LeftBlackByoYomiMoves,
        OW: SGFGoNodeProperty.LeftWhiteByoYomiMoves,
        WL: SGFGoNodeProperty.WhiteLeftTime,

        //Miscellaneous properties
        FG: SGFGoNodeProperty.BoardLayoutProperties,
        PM: SGFGoNodeProperty.MovePrintingProperties,
        VM: SGFGoNodeProperty.ViewPartOfBoard,

        unknown: { //some kind of error?
        },

        getSgfProperty: function(propName) {
            var p = this[propName];
            if (!p) {
                p = this.unknown;
            }
            return p;
        }

    }

    /*
     * Factory for building an SGF Go Tree
     */
    function SGFGoBuilder(){
        this.goTree = new GoTree();
    }

    SGFGoBuilder.prototype = new GoBuilder();

    SGFGoBuilder.prototype.constructor = SGFBuilder;

    SGFGoBuilder.prototype.build = function(branches){
        this.sgfTree = new SGFTree(branches);
    };

    SGFGoBuilder.prototype.buildBranch = function(nodes){
        //push branch
    };

    SGFGoBuilder.prototype.buildNode = function(props: {} branches){
        //push node
    };

    SGFGoBuilder.prototype.buildNodeProp = function(name: {} value){
        //push node prop
    };

    SGFGoBuilder.prototype.getGoTree = function() {
        return this.sgfTree;
    }

