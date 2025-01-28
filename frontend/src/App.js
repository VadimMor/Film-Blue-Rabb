import React, { Component } from 'react';
import {
  Route,
  BrowserRouter,
  Routes,
} from 'react-router-dom';

import Header from './components/Header/Header';
import Main from './pages/Main/Main';

function App() {
  return (
    <BrowserRouter>
        <Header />
        <div className='main'>
          <Routes>
            <Route path="/" element={<Main />} />
          </Routes>
        </div>
    </BrowserRouter>
  )
}

export default App;
