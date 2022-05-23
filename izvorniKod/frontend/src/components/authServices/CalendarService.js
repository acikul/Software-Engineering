import axios from 'axios'

async function CalendarService() {
    try {
        axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('token')
        let response = await axios.get('v1/sportevents/calendar')

        console.log(response.data)

        const events = response.data.map(sevent => (
            {
                id: sevent.id,
                cost: sevent.cost,
                title: sevent.sport,
                location: sevent.location,
                start: sevent.startDateTime,
                end: sevent.endDateTime,
                formattedStart: '',
                formattedEnd: '',
                borderColor: sevent.cost ? "#FF4500" : "#2FFE0E",
                participants: sevent.participents.length, // + organizator(mozda)
                organizer: sevent.organizer,
                maxNumberOfParticipants: sevent.maxNumberOfParticpents,
            }
        ))

        for (let i = 0; i < events.length; i++) {
            let sportName = await axios.get("/v1/sports/" + events[i].title)
            if (sportName) {
                events[i].title = sportName.data.name
            }
        }

        return events
    } catch (err) {
        console.log(err)
    }
}

export default CalendarService