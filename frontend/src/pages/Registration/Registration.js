import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { getCookie } from '@shared/cookie/getCookie';

import classes from './Registration.module.css';

const Registration = () => {
    const navigate = useNavigate();

    useEffect(() => {
        if (getCookie('authToken')) {
            navigate(-1);
        }
    }, []);

    return (
        <div className="wrapper">
            d
        </div>
    )
}

export default Registration;