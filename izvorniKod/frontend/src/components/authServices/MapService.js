import axios from 'axios'

async function MapService(coordinates) {
    try {
        let response = await axios.post("/v1/sportevents/map", 
                                            {
                                                topRightLatitude:coordinates.topRightLatitude,
                                                topRightLongitude:coordinates.topRightLongitude,
                                                bottomLeftLatitude:coordinates.bottomLeftLatitude,
                                                bottomLeftLongitude:coordinates.bottomLeftLongitude,
                                            }
                                        )
        const events = response.data
        for (let i = 0; i<events.length; i++) {
            let sportName = await axios.get("/v1/sports/" + events[i].sport)
            if (sportName) {
                events[i].sport = sportName.data.name
            }
        }
        console.log(events)
        return events
    } catch(err) {
        console.log(err)
    }
}

export default MapService