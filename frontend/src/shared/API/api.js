import axios from 'axios';

export const getApi = () => {
    const url = 'http://localhost:8080'

    async function getAuth(login, pass) {
        const obj = {
            "login": login,
            "password": pass
        }
        
        const data = await axios.post(
            `${url}/api/security/auth`,
            obj
        )
        
        return data.data
    }

    return {
        getAuth
    }
}