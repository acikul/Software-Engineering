import axios from 'axios'

async function LocationService(id) {
    try {
        let response = await axios.get("/v1/locations/" + id)
        const newGPXCoordinates = response.data.gpxCoordinates.split("").filter(c => {return c!=="[" && c!=="]"}).join("").split(",")
        response.data.gpxCoordinates = []
        for (let i = 0; i<newGPXCoordinates.length; i=i+2) {
             response.data.gpxCoordinates.push([newGPXCoordinates[i],newGPXCoordinates[i+1]])
        }
        return response.data
    } catch(err) {
        console.log(err)
    }
}

export default LocationService