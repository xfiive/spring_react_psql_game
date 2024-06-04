import React, {useCallback, useContext, useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import ValueContext from '../ValueContext';
import axios from 'axios';
import Board from "../Board";
import HttpComponent from './HttpComponent';
import Background from '../authentication/Background';
import styles from './GameCore.module.css';
import AudioPlayer from "../AudioPlayer";

const GameCore = () => {
    const {
        gameState, setGameState,
        gameWonBy, setGameWonBy,
        currentTurn, setCurrentTurn,
        player1Nickname, setPlayer1Nickname,
        player2Nickname, setPlayer2Nickname,
        initialCheckers, setInitialCheckers
    } = useContext(ValueContext);

    const [lastMoveResult, setLastMoveResult] = useState('UNDEFINED');
    const [winner, setWinner] = useState(null);
    const [selectedChecker, setSelectedChecker] = useState(null);
    const navigate = useNavigate();

    const updateCheckers = useCallback((startPosition, endPosition) => {
        setInitialCheckers(prevCheckers => {
            const newCheckers = [...prevCheckers];

            const checkerIndex = newCheckers.findIndex(checker => checker.id === startPosition);

            if (checkerIndex !== -1) {
                const currentChecker = newCheckers.at(checkerIndex);
                currentChecker.id = endPosition;
            }

            return newCheckers;
        });
    }, [setInitialCheckers]);

    const removeCheckers = useCallback((positionsToRemove) => {
        setInitialCheckers(prevCheckers => {
            const positionsArray = Array.isArray(positionsToRemove) ? positionsToRemove : [positionsToRemove];
            console.log('Current checkers before removal: ', prevCheckers);
            const newCheckers = prevCheckers.filter(checker => !positionsArray.includes(checker.id));
            console.log('Checkers after removal: ', newCheckers);
            return newCheckers;
        });
    }, [setInitialCheckers]);


    const sendMove = useCallback(async (player, from, to) => {
        try {
            const response = await axios.post('http://localhost:8080/game/move', {
                player,
                move: `${from}${to}`,
            });
            const data = response.data;
            console.log('Data checkers to remove: ' + data.checkerToRemoveCoordinate);
            if (data.isMoveValid === 'true') {
                updateCheckers(from, to);
                console.log('Checkers to remove: ', data.checkerToRemoveCoordinate);
                if (data.checkerToRemoveCoordinate) {
                    const checkersToRemove = JSON.parse(data.checkerToRemoveCoordinate);
                    removeCheckers(checkersToRemove);
                }
            } else {
                console.error('Invalid move attempted');
            }
            console.log('Game turn from server: ' + data.gameTurn);
            setCurrentTurn(data.gameTurn);
            setGameState(data.gameStatus);
            if (data.gameStatus === 'Won') {
                setGameWonBy(data.gameWonBy);
                navigate('/game/victory');
            }
        } catch (error) {
            console.error('Error sending move:', error);
        }
    }, [updateCheckers, removeCheckers]);

    useEffect(() => {
        console.log('Current game turn in useEffect: ' + currentTurn);
        console.log('Game won by: ' + winner);
    }, [currentTurn]);

    const handleMoveMade = useCallback((moveResponse) => {
        console.log('Server responded with:', moveResponse);
        if (moveResponse.moveStatus === 'first') {
            setCurrentTurn('White');
        } else if (moveResponse.moveStatus === 'second') {
            setCurrentTurn('Black');
        }
        setLastMoveResult(moveResponse.moveStatus);
        if (moveResponse.gameStatus) {
            setGameState(moveResponse.gameStatus);
        }
    }, []);

    const handleCheckerClick = useCallback((id, color) => {
        if (!selectedChecker) {
            setSelectedChecker(initialCheckers.find(checker => checker.id === id && checker.color === color));
        } else {
            if (!initialCheckers.some(checker => checker.id === id)) {
                const currentPlayerName = currentTurn === 'White' ? player1Nickname : player2Nickname;
                sendMove(currentPlayerName, selectedChecker.id, id);
                setSelectedChecker(null);
            } else {
                setSelectedChecker(null);
            }
        }
    }, [selectedChecker, initialCheckers, currentTurn, sendMove]);

    const resetSession = async () => {
        try {
            const response = await fetch('http://localhost:8080/game/reset-session', {
                method: 'POST'
            });
            if (response.ok) {
                console.log('Session has been reset successfully');
            } else {
                console.error('Failed to reset session:', response.status);
            }
        } catch (error) {
            console.error('Error resetting session:', error);
        }
    };

    const navigateToStartPage = () => {
        resetSession();
        navigate('/menu');
    };

    const navigateToMainMenu = () => {
        resetSession();
        navigate('/menu/start');
    };

    const handleBackToStart = () => {
        resetSession();
        navigateToStartPage();
    };

    const handleBackToMenu = () => {
        resetSession();
        navigateToMainMenu();
    };

    const handleSurrender = async () => {
        const currentPlayerName = currentTurn === 'White' ? player1Nickname : player2Nickname;
        try {
            const response = await axios.post(`http://localhost:8080/game/surrender?player1Name=${encodeURIComponent(currentPlayerName)}`);
            console.log('Surrender successful:', response.data);
            navigate('/game/victory', {state: {surrendered: true, player: currentPlayerName}});
        } catch (error) {
            console.error('Error during surrender:', error);
        }
    };


    return (
        <div>
            <Background>
                <AudioPlayer/>
                <div className={styles['game-status'] + ' ' + styles[currentTurn.toLowerCase() + '-turn']}>
                    {currentTurn === 'White' ? 'White Turn' : 'Black Turn'}
                </div>
                <Board handleCheckerClick={handleCheckerClick}/>
                <HttpComponent onMoveMade={handleMoveMade}/>
                <div style={{
                    position: 'fixed',
                    left: '50%',
                    bottom: '20px',
                    transform: 'translateX(-50%)',
                    display: 'flex',
                    justifyContent: 'center',
                    width: 'auto'
                }}>
                    <button style={{
                        minWidth: '120px',
                        padding: '10px 20px',
                        border: 'none',
                        borderRadius: '5px',
                        backgroundColor: '#4C5C68',
                        color: 'white',
                        fontSize: '16px',
                        cursor: 'pointer',
                        margin: '0 10px'
                    }} onClick={handleBackToStart}>Back to Start
                    </button>
                    <button style={{
                        minWidth: '120px',
                        padding: '10px 20px',
                        border: 'none',
                        borderRadius: '5px',
                        backgroundColor: '#4C5C68',
                        color: 'white',
                        fontSize: '16px',
                        cursor: 'pointer',
                        margin: '0 10px'
                    }} onClick={handleBackToMenu}>Back to Menu
                    </button>
                    <button style={{
                        minWidth: '120px',
                        padding: '10px 20px',
                        border: 'none',
                        borderRadius: '5px',
                        backgroundColor: '#4C5C68',
                        color: 'white',
                        fontSize: '16px',
                        cursor: 'pointer',
                        margin: '0 10px'
                    }} onClick={handleSurrender}>Surrender
                    </button>
                </div>
            </Background>
        </div>
    );

}

const NavigationPanel = ({onBackToStart, onBackToMenu}) => (
    <div style={{
        position: 'fixed',
        left: '50%',
        bottom: '20px',
        transform: 'translateX(-50%)',
        display: 'flex',
        justifyContent: 'center',
        width: 'auto'
    }}>
        <button style={{
            minWidth: '120px',
            padding: '10px 20px',
            border: 'none',
            borderRadius: '5px',
            backgroundColor: '#4C5C68',
            color: 'white',
            fontSize: '16px',
            cursor: 'pointer',
            margin: '0 10px'
        }} onClick={onBackToStart}>Back to start
        </button>
        <button style={{
            minWidth: '120px',
            padding: '10px 20px',
            border: 'none',
            borderRadius: '5px',
            backgroundColor: '#4C5C68',
            color: 'white',
            fontSize: '16px',
            cursor: 'pointer',
            margin: '0 10px'
        }} onClick={onBackToMenu}>Back to menu
        </button>
    </div>
);


export default GameCore;