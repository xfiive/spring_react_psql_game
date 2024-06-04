import axios from 'axios';

const SCORES_REST_API_URL = 'http://localhost:8080/api/score/checkers';

class ScoreService {
    getScores() {
        return axios.get(SCORES_REST_API_URL);
    }
}

export default new ScoreService();