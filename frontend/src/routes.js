import React from 'react';
import { Routes, Route } from 'react-router-dom';

import Home from './pages/Home/Home';
import E404 from './pages/404/E404';

const RoutesConfig = () => {
    return (
        <main>
            <Routes>
                <Route
                    path="/" 
                    element={
                        <Home />
                    } />
                <Route
                    path="/*" 
                    element={
                        <E404 />
                    } />
            </Routes>
        </main>
    )
}

export default RoutesConfig;