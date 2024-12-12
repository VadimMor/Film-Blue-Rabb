import axios from 'axios';

export const getApi = () => {
  
    const url = 'http://go-ticket-bar.online:8443'
  
    async function getAuth(email, pass, role) {
        const obj = {
			email: email,
			password: pass,
            role: role
		}
        
        const data = await axios.post(
            `${url}/security/auth`,
            obj
        )

        return data.data;
    }
    
    async function getAnnouncements(city, date, token = null) {
        if (token == null) {
            
            const data = await axios.get(
                `${url}/event/${city}/announcement?offset=0&date=${date}`
            )
            
            return data.data.public_events
        } else {
            
            const data = await axios.get(
                `${url}/event/${city}/announcement?offset=0&date=${date}`,
                {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }
            )
            
            return data.data.public_events
        }
    }

    async function getEvents(city, date, token = null, offset) {
        if (token == null) {
            const data = await axios.get(
                `${url}/event/${city}?offset=${offset}&date=${date}`
            )
            console.log(data.data)
            return data.data.public_events
        } else {
            const data = await axios.get(
                `${url}/event/${city}?offset=${offset}&date=${date}`,
                {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }
            )
            
            return data.data.public_events
        }
    }

    async function getEventInfo(city, date, eventName, token = null) {
        if (token == null) {
            const data = await axios.get(
                `${url}/event/${city}/${eventName}?date=${date}`
            )

            return data.data
        } else {
            const data = await axios.get(
                `${url}/event/${city}/${eventName}?date=${date}`,
                {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }
            )

            return data.data
        }
    }

    async function putToggleFavorite(token, event) {
        const data = {
            event_info: null
        }

        await axios(
            {
                method: "put",
                url: `${url}/client/favorite?id=${event}`,
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        ).then(response => {
            data.event_info = response.data
        })
        return (data.event_info)
    }

    async function getPlacesOrganization(token) {
        const data = await axios.get(
            `${url}/organization/places`,
            {
                headers: {
                    "Authorization" : `Bearer ${token}`
                }
            }
        )

        return data.data
    }

    async function getAllCity() {
        const data = await axios.get(
            `${url}/place/city`
        )

        return data.data
    }
    
    async function createPlace(token, city, address, name) {
        const obj = {
            "address": address,
            "place_name": name,
            "city": city.name_eng
        }
        
        const data = await axios.post(
            `${url}/place`,
            obj,
            {   
                headers: {
                    "Authorization" : `Bearer ` + token
                }
            }
        )
        
        return data.data
    }

    async function getHallsOrganization(token) {
        const data = await axios.get(
            `${url}/organization/halls`,
            {
                headers: {
                    "Authorization" : `Bearer ${token}`
                }
            }
        )
        
        return data.data
    }

    async function createHall(token, obj) {
        const data = await axios.post(
            `${url}/place/hall`,
            obj,
            {   
                headers: {
                    "Authorization" : `Bearer ` + token
                }
            }
        )

        return data.data
    }

    async function getEventsOrganization(token) {
        const data = await axios.get(
            `${url}/organization/events`,
            {
                headers: {
                    "Authorization" : `Bearer ${token}`
                }
            }
        )

        return data.data
    }
    
    async function getSessionsOrganization(token) {
        const data = await axios.get(
            `${url}/organization/session_sum`,
            {
                headers: {
                    "Authorization" : `Bearer ${token}`
                }
            }
        )

        return data.data
    }

    async function getTypes() {
        const data = await axios.get(
            `${url}/event/type`
        )

        return data.data
    }

    async function createSession(token, obj) {
        const data = await axios.post(
            `${url}/session`,
            {
                "start_time": new Date(obj.start),
                "price": obj.price,
                "event_id": obj.event.id,
                "hall_id": obj.hall.hall_id,
                "type_of_movie": obj.type.type_name
            },
            {   
                headers: {
                    "Authorization" : `Bearer ` + token
                }
            }
        )
        
        return data.data
    }

    async function getMonthlySalesOrganization(token) {
        const data = await axios.get(
            `${url}/organization/sales/month`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        )
        return data
    }

    async function createEvent(token, obj) {
        const data = await axios.post(
            `${url}/event`,
            obj,
            {   
                headers: {
                    "Authorization" : `Bearer ` + token
                }
            }
        )

        return data.data
    }

    async function getMonthlySessionsOrganization(token) {
        const data = await axios.get(
            `${url}/organization/sessions/month`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        )

        return data.data
    }

    async function getYearlySalesOrganization(token) {
        const data = await axios.get(
            `${url}/organization/sales/year`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        )

        return data.data
    }

    async function getSheme(id) {
        const data = await axios.get(
            `${url}/session/scheme?id=${id}`
        )

        return data.data
    }

    async function getTicketsUser(token) {
        const data = await axios.get(
            `${url}/client/tickets`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        )

        return data.data
    }

    async function getInfoClient(token) {
        const data = await axios.get(
            `${url}/client`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        )

        return data.data
    }

    async function postClient(body) {
        const data = await axios.post(
            `${url}/security`,
            body
        )

        return data.data
    }

    async function putActiveClient(body) {
        const data = await axios.put(
            `${url}/security/active`,
            body
        )

        return data.data
    }

    return {
        getAnnouncements,
        getEvents,
        getEventInfo,
        getAuth,
        putToggleFavorite,
        getPlacesOrganization,
        getAllCity,
        createPlace,
        getHallsOrganization,
        createHall,
        getEventsOrganization,
        getSessionsOrganization,
        getTypes,
        createSession,
        getMonthlySalesOrganization,
        getMonthlySessionsOrganization,
        getYearlySalesOrganization,
        getSheme,
        getTicketsUser,
        getInfoClient,
        postClient,
        putActiveClient,
        createEvent
    }
}