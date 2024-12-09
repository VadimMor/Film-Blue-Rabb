import React from 'react';

import classes from './E404.module.css';

const E404 = () => {
  return (
    <div className={classes.e404 + ' wrapper'}>
        <h4 className={classes.e404_title}>404</h4>
        <div className={classes.e404_text}>Страница не найдена</div>
    </div>
  );
};

export default E404;