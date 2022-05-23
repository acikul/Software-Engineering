import axios from 'axios'

async function DataService() {
    try {
        axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('token')
        let response = await axios.get('/v1/me')
        console.log(response.data)
        return response.data
    }
    catch (err) {
        console.log(err)
        return null
    }
}

export default DataService