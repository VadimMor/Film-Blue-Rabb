import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { getApi } from '@shared/API/api';
import { setCookie } from '@shared/cookie/setCookie';
import { getCookie } from '@shared/cookie/getCookie';

import classes from './Auth.module.css';

const api = getApi();

const pattern = {
    login: /(\b[A-Za-z0-9._%@+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,})|(^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{5,28}$)/,
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,25}$/
}

function validateObject (obj) {
    const errors = {}

    for (let key in obj) {
        if (
            (!obj[key]) || 
            (obj[key].trim() === '') ||
            (!pattern[key].test(obj[key]))
        ) {
            errors[key] = true
        } else {
            errors[key] = false
        }
    }

    return errors;
}

const Auth = () => {
    const [pass, setPass] = useState(true);
    const [formData, setFormData] = useState({
        login: '',
        password: ''
    })
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    function togglePassword() {
        setPass(!pass)
    }

    // Сохранения введеного текста в form
    function handleChange (e) {
        const {name, value} = e.target;
        setFormData({...formData, [name]: value});
    }

    // Авторизация полдьзователя
    async function getAuth(e) {
        e.preventDefault();
        const validationErrors = validateObject(formData);
        
        // Установить ошибки в состояние
        setErrors(validationErrors);

        // Проверить, есть ли ошибки
        const hasErrors = Object.values(validationErrors).some(error => error);

        if (hasErrors) {
            return; // Прервать выполнение, если есть ошибки
        }
        
        try {
            const response = await api.getAuth(
                formData.login,
                formData.password
            );
            
            setCookie('authToken', response.token);
            navigate('/')
        } catch (error) {
            if (error.response.data.status === 401) {
                setErrors({ login: true, password: true, all: true });
            } else {
                console.error('Error during authorization:', error);
            }
        }
    }

    return (
        <div className=' wrapper'>
            <div className={classes.auth}>
                <div className={classes.auth_container}>
                    <h3 className={classes.title}>Авторизация</h3>
                    
                    {
                        errors.all ?
                            <div className={classes.err_form}>Неверный логин или пароль</div> :
                            null
                    }

                    <div className={classes.auth_form}>
                        <label className={classes.label}>Логин:
                            <input 
                                className={
                                    errors.login ? 
                                    `ERROR ${classes.input}` :
                                    `${classes.input}`
                                }
                                placeholder="Логин"
                                name="login"
                                value={formData.login}
                                onChange={handleChange}
                            />
                        </label>
                        
                        
                        <label className={classes.label}>Пароль:
                            <input 
                                className={
                                    errors.password ? 
                                    `ERROR ${classes.input}` :
                                    `${classes.input}`
                                }
                                type={
                                    pass ?
                                        "password" :
                                        "text"
                                }
                                placeholder="Пароль"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                            />
                            <span 
                                className={
                                    pass ?
                                        `${classes.eye} icon-eye` :
                                        `${classes.eye} icon-eye-off`
                                }
                                onClick={togglePassword}
                            />
                        </label>
                        

                        <button
                            className={`${classes.btn} ${classes.btn_auth}`}
                            onClick={getAuth}
                        >
                            Войти
                        </button>
                    </div>
                    
                    <span className={classes.or}>или</span>

                    <Link
                        to={'/reg'}
                        className={`${classes.btn} ${classes.btn_reg}`}
                        title="Регистрация"
                    >
                        Регистрация
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default Auth;