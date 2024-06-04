import React from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import {ValueProvider} from './components/ValueContext';
import GameCore from './components/game/GameCore';
import MainMenu from './components/game/MainMenu';
import StartPage from './components/StartPage';
import HallOfFame from '../src/components/game/HallOfFame';
import GameEndForm from "./components/game/GameEndForm";
import Background from "./components/authentication/Background";
import About from './components/About';

const App = () => {
    return (
        <ValueProvider>
            <Background>
                <Router>
                    <Routes>
                        <Route path="/" element={<Navigate replace to="/menu"/>}/>
                        <Route path="/game" element={<GameCore/>}/>
                        <Route path="/game/victory" element={<GameEndForm/>}/>
                        <Route path="/menu" element={<StartPage/>}/>
                        <Route path="/menu/start" element={<MainMenu/>}/>
                        <Route path="/hof" element={<HallOfFame/>}/>
                        <Route path="/about" element={<About/>}/>
                        <Route path="*" element={<Navigate to="/menu" replace/>}/>
                    </Routes>
                </Router>
            </Background>
        </ValueProvider>
    );
};

export default App;
