import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import Header from './components/Header/Header';

import RoutesConfig from './routes';

function App() {
  return (
    <Router>
      <Header />

      <RoutesConfig />
    </Router>
  );
}

export default App;