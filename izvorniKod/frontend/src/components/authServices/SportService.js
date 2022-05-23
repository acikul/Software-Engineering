import axios from 'axios'

async function SportService() {
    try {
        let response = await axios.get("/v1/sports")
        return response.data
    } catch(err) {
        console.log(err)
    }
}

export default SportService