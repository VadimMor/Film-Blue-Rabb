import React from "react";
import { NavLink } from "react-router-dom";

import classes from './NavLinks.module.css';

const NavLinks = ({path, title}) => {
    return (
        <NavLink
            to={path}
            title={title}
            className={classes.footer_link}
        >
            {title}
        </NavLink>
    )
}

export default NavLinks;