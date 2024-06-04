import React, {useContext, useState, useEffect} from 'react';
import {useNavigate, useLocation} from "react-router-dom";
import ValueContext from '../ValueContext';
import styles from './GameEndForm.module.css';
import axios from "axios";
import {formatISO} from 'date-fns';

const GameEndForm = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const {
        gameState, setGameState,
        gameWonBy, setGameWonBy,
        currentTurn, setCurrentTurn,
        player1Nickname, setPlayer1Nickname,
        player2Nickname, setPlayer2Nickname,
        initialCheckers, setInitialCheckers
    } = useContext(ValueContext);

    const endGameMessage = gameState === 'Won' ? 'Game Over!' : 'Game Over!';
    const winnerMessage = gameState === 'Won' ? `White Wins!` : 'Black Wins!';

    const gameName = "YourGameName";

    const [comment1, setComment1] = useState('');
    const [rating1, setRating1] = useState('1');
    const [comment2, setComment2] = useState('');
    const [rating2, setRating2] = useState('1');


    const state = location.state;

    const resetGame = async () => {
        setGameState(null);
        setGameWonBy(null);
        setPlayer1Nickname('');
        setPlayer2Nickname('');
        setInitialCheckers([]);
        navigate('/menu');
    };

    useEffect(() => {
        if (state?.surrendered) {
            const message = `${state.player} has surrendered. `;
            alert(message);
        }
    }, [state]);

    const handleAddComment = async (nickname, comment, game) => {
        const commentedOn = formatISO(new Date(), {representation: 'date'});
        try {
            const response = await axios.post(`http://localhost:8080/api/comment/set`, {
                player: nickname,
                game,
                comment,
                commentedon: commentedOn
            });
            console.log(response.status === 200 ? 'Comment added successfully' : `Failed to add comment, status: ${response.status}`);
        } catch (error) {
            console.error('Failed to add comment:', error);
        }
    };

    const handleAddRating = async (nickname, rating, game) => {
        const ratedOn = formatISO(new Date(), {representation: 'date'});
        try {
            const response = await axios.post(`http://localhost:8080/api/rating/set`, {
                player: nickname,
                game,
                rating,
                ratedon: ratedOn
            });
            console.log(response.status === 200 ? 'Rating added successfully' : `Failed to add rating, status: ${response.status}`);
        } catch (error) {
            console.error('Failed to add rating:', error);
        }
    };

    const handleAddComment1 = () => {
        console.log('Attempting to add comment for player1');
        handleAddComment('q', comment1, 'checkers');
        setComment1('');
    };

    const handleAddRating1 = () => {
        console.log('Attempting to add rating for player1');
        handleAddRating('q', rating1, 'checkers');
        setRating1(5);
    };

    const handleAddComment2 = () => {
        console.log('Attempting to add comment for player2');
        handleAddComment('w', comment2, 'checkers');
        setComment2('');
    };

    const handleAddRating2 = () => {
        console.log('Attempting to add rating for player2');
        handleAddRating('w', rating2, 'checkers');
        setRating2(5);
    };

    const navigateToStartPage = () => navigate('/menu');
    const navigateToMainMenu = () => navigate('/menu/start');

    const resetSession = async () => {
        try {
            const response = await fetch(`http://localhost:8080/game/reset-session`, {
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

    const handleBackToStart = () => {
        resetSession();
        navigateToStartPage();
    };

    const handleBackToMenu = () => {
        resetSession();
        navigateToMainMenu();
    };

    return (
        <div className={styles.background}>
            <PlayerPanel
                nickname={player1Nickname}
                comment={comment1}
                setComment={setComment1}
                rating={rating1}
                setRating={setRating1}
                onAddComment={handleAddComment1}
                onAddRating={handleAddRating1}
            />
            <div className={styles.modal}>
                <h1 className={styles.title}>{endGameMessage}</h1>
                <p className={styles.winnerMessage}>{winnerMessage}</p>
            </div>
            <PlayerPanel
                nickname={player2Nickname}
                comment={comment2}
                setComment={setComment2}
                rating={rating2}
                setRating={setRating2}
                onAddComment={handleAddComment2}
                onAddRating={handleAddRating2}
            />
            <NavigationPanel handleBackToStart={handleBackToStart} handleBackToMenu={handleBackToMenu}/>
        </div>

    );

}

const NavigationPanel = ({handleBackToStart, handleBackToMenu}) => (
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
        }} onClick={handleBackToStart}>Back to start
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
        }} onClick={handleBackToMenu}>Back to menu
        </button>
    </div>
);


const PlayerPanel = ({nickname, comment, setComment, rating, setRating, onAddComment, onAddRating}) => (
    <div className={styles.playerPanel}>
        <h2>{nickname}</h2>
        <div className={styles.inputContainer}>
            <input
                type="text"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder="Enter your comment"
            />
            <button className={styles.button} onClick={() => onAddComment()}>Add comment</button>
        </div>
        <div className={styles.inputContainer}>
            <select value={rating} onChange={(e) => setRating(e.target.value)}>
                {Array.from({length: 5}, (_, i) => <option key={i} value={i + 1}>{i + 1}</option>)}
            </select>
            <button className={styles.button} onClick={() => onAddRating()}>Add rating</button>
        </div>
    </div>
);

export default GameEndForm;
