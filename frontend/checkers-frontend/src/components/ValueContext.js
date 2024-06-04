import React, {createContext, useState} from 'react';

const ValueContext = createContext({
    gameStarted: false,
    gameWonBy: 'undefined',
    setGameWonBy: () => {
    },
    gameState: 'notStarted',
    setGameState: () => {
    },
    currentTurn: 'White',
    setCurrentTurn: () => {
    },
    player1Nickname: '',
    player2Nickname: '',
    startGame: () => {
    },
    setPlayer1Nickname: () => {
    },
    setPlayer2Nickname: () => {
    },
    initialCheckers: [],
    setInitialCheckers: () => {
    }
});

export const ValueProvider = ({children}) => {
    const [gameStarted, setGameStarted] = useState(false);
    const [gameWonBy, setGameWonBy] = useState('undefined');
    const [gameState, setGameState] = useState('notStarted');
    const [currentTurn, setCurrentTurn] = useState('White');
    const [player1Nickname, setPlayer1Nickname] = useState('');
    const [player2Nickname, setPlayer2Nickname] = useState('');
    const [initialCheckers, setInitialCheckers] = useState([
        {id: 'b8', color: 'black'}, {id: 'd8', color: 'black'}, {id: 'f8', color: 'black'}, {id: 'h8', color: 'black'},
        {id: 'a7', color: 'black'}, {id: 'c7', color: 'black'}, {id: 'e7', color: 'black'}, {id: 'g7', color: 'black'},
        {id: 'b6', color: 'black'}, {id: 'd6', color: 'black'}, {id: 'f6', color: 'black'}, {id: 'h6', color: 'black'},
        {id: 'a3', color: 'white'}, {id: 'c3', color: 'white'}, {id: 'e3', color: 'white'}, {id: 'g3', color: 'white'},
        {id: 'b2', color: 'white'}, {id: 'd2', color: 'white'}, {id: 'f2', color: 'white'}, {id: 'h2', color: 'white'},
        {id: 'a1', color: 'white'}, {id: 'c1', color: 'white'}, {id: 'e1', color: 'white'}, {id: 'g1', color: 'white'}
    ]);

    return (
        <ValueContext.Provider value={{
            gameStarted,
            gameWonBy,
            setGameWonBy,
            gameState,
            setGameState,
            currentTurn,
            setCurrentTurn,
            player1Nickname,
            player2Nickname,
            startGame: () => setGameStarted(true),
            setPlayer1Nickname,
            setPlayer2Nickname,
            initialCheckers,
            setInitialCheckers
        }}>
            {children}
        </ValueContext.Provider>
    );
};

export default ValueContext;
