import React, {useCallback, useEffect} from 'react';
import axios from 'axios';

const HttpComponent = ({onMoveMade, sendMove}) => {

    // const sendMove = useCallback((player, from, to) => {
    //     console.log("Sending move to server...");
    //     axios.post('http://localhost:8080/game/move', {
    //         player: player,
    //         move: `${from}-${to}`,
    //     }).then(response => {
    //         console.log('Move response:', response.data);
    //         onMoveMade(response.data);
    //     }).catch(error => {
    //         console.error('Error sending move:', error);
    //     });
    // }, [onMoveMade]);

    return null;
};
export default HttpComponent;
