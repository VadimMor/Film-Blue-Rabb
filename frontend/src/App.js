import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import Header from './components/Header/Header';

import RoutesConfig from './routes';
import Footer from './components/Footer/Footer';

function App() {
  return (
    <Router>
      <Header />

      <RoutesConfig />

      <Footer />
    </Router>
  );
}

export default App;