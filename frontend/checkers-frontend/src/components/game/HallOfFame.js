import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import styles from './HallOfFame.module.css';

const HallOfFame = ({game}) => {
    const [comments, setComments] = useState([]);
    const [averageRating, setAverageRating] = useState(null);
    const [topScores, setTopScores] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const navigate = useNavigate();

    const formatDate = (dateString) => {
        if (!dateString) {
            return 'Date not available';
        }

        const date = new Date(dateString);
        if (isNaN(date)) {
            console.error('The date is invalid:', dateString);
            return 'Date not available';
        }

        const options = {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            timeZoneName: 'short'
        };
        return date.toLocaleString('en-US', options);
    };

    const goBack = () => {
        navigate('/menu');
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [commentsRes, averageRatingRes, topScoresRes] = await Promise.all([
                    axios.get(`http://localhost:8080/api/comment/checkers`),
                    axios.get(`http://localhost:8080/api/rating/checkers/average`),
                    axios.get(`http://localhost:8080/api/score/checkers`)
                ]);

                const dateRequests = commentsRes.data.map(comment =>
                    axios.get(`http://localhost:8080/api/comment/checkers/date/${comment.player}/string`)
                        .then(response => ({...comment, commentedOn: response.data}))
                        .catch(() => ({...comment, commentedOn: 'Date not available'}))
                );

                const commentsWithDates = await Promise.all(dateRequests);

                setComments(commentsWithDates);
                setAverageRating(averageRatingRes.data);
                setTopScores(topScoresRes.data);

            } catch (error) {
                setError('Failed to fetch data from the server');
                console.error('API fetch error:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className={styles.background}>
            <h1 className={`${styles.header} ${styles.title}`}>Hall of Fame</h1>
            <div className={`${styles.container} ${styles.scoreContainer}`}>
                <h2 className={styles.title}>Top Scores for {game}</h2>
                <ul className={styles.scoreList}>
                    {topScores.map((score, index) => (
                        <li key={index} className={styles.text}>
                            {score.player} :     {score.points}
                        </li>
                    ))}
                </ul>
            </div>
            <div className={`${styles.container} ${styles.commentContainer}`}>
                <h2 className={styles.title}>Comments</h2>
                <ul className={styles.commentsList}>
                    {comments.map((comment, index) => (
                        <li key={index} className={styles.text}>
                            <span>{comment.player}:</span>
                            <span>{comment.comment}</span>
                            <span
                                className={styles.commentDate}>   --> was given on   {formatDate(comment.commentedOn)}</span>
                        </li>
                    ))}
                </ul>
            </div>
            <div className={`${styles.container} ${styles.averageRatingContainer}`}>
                <h2 className={styles.title}>Average Rating</h2>
                <div className={`${styles.text} ${styles.averageRating}`}>{averageRating}</div>
            </div>
            <div className={styles.buttonContainer}>
                <button onClick={goBack} className={styles.backButton}>Back</button>
            </div>
        </div>
    );
};

export default HallOfFame;
