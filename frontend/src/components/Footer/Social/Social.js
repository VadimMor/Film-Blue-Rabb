import React from "react";
import { Link } from "react-router-dom";

import classes from './Social.module.css';

const Social = ({path, icon, title}) => {
    return (
        <Link
            to={path}
            target="_blank"
            className={
                classes.social + ' icon-' + icon
            }
            title={title}
        />
    )
}

export default Social