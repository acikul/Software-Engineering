import React from 'react'
import axios from 'axios'
import FullCalendar, { CalendarContent } from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import './RCalendar.css'
import { Link, Redirect } from 'react-router-dom'


class RCalendar extends React.Component {

    constructor() {
        super()
        this.state = {
            weekendsVisible: true,
            currentEvent: '',
            showEvent: false,
            locationsData: [],
            eventsData: [],
            userEventStatus: false,
            clicked: false,
            avoidInf: false,
        }

        this.handleClick = this.handleClick.bind(this)
        this.changeClick = this.changeClick.bind(this)
        this.handleRequests = this.handleRequests.bind(this)
        this.handleEventClick = this.handleEventClick.bind(this)
        this.acceptRequest = this.acceptRequest.bind(this)
        this.denyRequest = this.denyRequest.bind(this)
        this.deleteConfirmation = this.deleteConfirmation.bind(this)
        this.dateFormatter = this.dateFormatter.bind(this)

    }

    componentDidMount() {
        if (localStorage.getItem("IDS") === null) {
            var IDS = [];
            localStorage.setItem("IDS", JSON.stringify(IDS));
        }
    }

    changeClick() {
        this.setState({ clicked: !this.state.clicked })
    }

    async handleRequests(locId) {
        try {
            var events = []

            const requests = await axios.get(`v1/users/${this.props.user.user_info.id}/locations/${locId}/reservations`)
            const reserved = await axios.get(`v1/users/${this.props.user.user_info.id}/locations/${locId}`)



            if (requests) {
                events = requests.data.map(sevent => (
                    {
                        id: sevent.id,
                        cost: sevent.cost,
                        title: sevent.sport,
                        location: sevent.location,
                        locationID: locId,
                        locationName: '',
                        start: sevent.startDateTime,
                        end: sevent.endDateTime,
                        formattedStart: '',
                        formattedEnd: '',
                        borderColor: sevent.cost ? "#FF4500" : "#2FFE0E",
                        participants: sevent.participents.length, // + organizator(mozda)
                        organizer: sevent.organizer,
                        maxNumberOfParticipants: sevent.maxNumberOfParticpents,
                        isConfirmed: false
                    }
                ))
                for (let i = 0; i < events.length; i++) {
                    let sportName = await axios.get("/v1/sports/" + events[i].title)
                    if (sportName) {
                        events[i].title = sportName.data.name
                    }
                }
            }
            events.push.apply(events, this.state.locationsData.find(x => x.id = locId).businessEvents)
            //console.log(this.state.locationsData.find(x => x.id = locId))
            console.log(reserved.data.holdsSportEvent)
            if (reserved.data.holdsSportEvent.length > 0) {
                for (let i = 0; i < reserved.data.holdsSportEvent.length; i++) {
                    let test = await axios.get(`/v1/sportevents/${reserved.data.holdsSportEvent[i]}`)
                    let a = JSON.parse(localStorage.getItem("IDS"))
                    let b = a.map(function (x) {
                        return parseInt(x, 10)
                    })
                    console.log(test.data.id)
                    if (!b.includes(test.data.id)) {
                        let sport = await axios.get(`/v1/sports/${test.data.sport}`)
                        let temp = {
                            id: test.data.id,
                            cost: test.data.cost,
                            title: sport.data.name,
                            location: test.data.location,
                            locationID: locId,
                            locationName: reserved.data.name,
                            start: test.data.startDateTime,
                            end: test.data.endDateTime,
                            formattedStart: '',
                            formattedEnd: '',
                            borderColor: "#FF4500",
                            participants: test.data.participents.length,
                            organizer: test.data.organizer,
                            maxNumberOfParticipants: test.data.maxNumberOfParticpents,
                            isConfirmed: true
                        }
                        events.push(temp)
                    }
                }
            }

            this.setState({
                eventsData: events,
                clicked: !this.state.clicked
            })


        } catch (err) {
            console.log(err)
        }
    }

    /*INITIAL_EVENTS = [
        {
            id: 1,
            title: 'Nogomet',
            description: 'Sportsko okupljanje: Nogomet',
            start: '2020-12-12T12:30:00',
            end: '2020-12-12T13:30:00',
            formattedStart: '',
            formattedEnd: '',
            location: 'Velesajam'
        },
        {
            id: 2,
            title: 'Veslanje',
            description: 'Profesionalni trening: Veslanje',
            start: '2020-12-13T08:00:00',
            end: '2020-12-13T10:00:00',
            formattedStart: '',
            formattedEnd: '',
            location: 'Jarun'
        }
    ]*/

    async handleEventClick(info) {
        const locations = await axios.get(`v1/users/${this.props.user.user_info.id}/locations/`)

        this.setState({ showEvent: !this.state.showEvent })
        this.setState({ currentEvent: info.event.toPlainObject({ collapseExtendedProps: true }) })
        this.setState({ clicked: false })

        for (let i = 0; i < locations.data.length; i++) {
            if (locations.data[i].id === this.state.currentEvent.locationID) {
                this.setState(prevState => {
                    let currentEvent = { ...prevState.currentEvent };
                    currentEvent.locationName = locations.data[i].name;
                    return { currentEvent }
                })
            }
        }


        var startTime = this.state.currentEvent.start;
        var endTime = this.state.currentEvent.end;

        if (startTime.includes("T") || endTime.includes("T")) {
            var startTimeNew = startTime.slice(11, 16);
            var endTimeNew = endTime.slice(11, 16);

            this.setState(prevState => {
                let currentEvent = { ...prevState.currentEvent };
                currentEvent.formattedStart = startTimeNew;
                currentEvent.formattedEnd = endTimeNew;
                return { currentEvent }
            })
        }

        if (this.state.showEvent === true) {
            window.scrollTo({
                top: 600,
                behavior: 'smooth'
            });
        }

    }

    async acceptRequest(eventInfo) {
        try {
            if (window.confirm("Jeste li sigurni da želite prihvatit ovaj zahtjev?")) {
                const userEventStatus = await axios.post(`/v1/users/${this.props.user.user_info.id}/locations/${eventInfo.locationID}/reservations/accept/${eventInfo.id}`)

                if (userEventStatus) {
                    this.setState({
                        userEventStatus: false,
                        showEvent: false
                    })
                    this.setState(prevState => {
                        let currentEvent = { ...prevState.currentEvent };
                        currentEvent.isConfirmed = true;
                        return { currentEvent }
                    })
                    alert("Zahtjev je prihvaćen.")
                    this.changeClick();
                    this.handleRequests(eventInfo.locationID)
                } else {
                    alert("Došlo je do pogreške, zahtjev nije prihvaćen")
                }
            }
        } catch (err) {
            alert("Došlo je do pogreške")
            console.log(err)
        }
    }

    async denyRequest(eventInfo) {
        try {
            if (window.confirm("Jeste li sigurni da želite odbiti ovaj zahtjev?")) {
                const userEventStatus = await axios.post(`/v1/users/${this.props.user.user_info.id}/locations/${eventInfo.locationID}/reservations/decline/${eventInfo.id}`)

                if (userEventStatus) {
                    this.setState({
                        userEventStatus: false,
                        showEvent: false
                    })
                    this.setState(prevState => {
                        let currentEvent = { ...prevState.currentEvent };
                        currentEvent.isConfirmed = false;
                        return { currentEvent }
                    })
                    alert("Zahtjev je odbijen.")
                    this.changeClick()
                    this.handleRequests(eventInfo.locationID)
                } else {
                    alert("Došlo je do pogreške, zahtjev nije odbijen")
                }
            }
        } catch (err) {
            alert("Došlo je do pogreške")
            console.log(err)
        }
    }

    async handleClick() {
        try {
            const locations = await axios.get(`v1/users/${this.props.user.user_info.id}/locations`, {})

            let test = await Promise.all(locations.data.map(async item => {
                let openings = await axios.get(`v1/users/${this.props.user.user_info.id}/locations/${item.id}/openings`)
                let openingsData = openings.data

                function getDaysBetweenDates(start, end, dayName) {
                    var start = new Date(start)
                    var end = new Date(end)
                    var result = [];
                    var days = { sun: 0, mon: 1, tue: 2, wed: 3, thu: 4, fri: 5, sat: 6 };
                    var day = days[dayName.toLowerCase().substr(0, 3)];
                    // Copy start date
                    var current = new Date(start);
                    // Shift to next of required days
                    current.setDate(current.getDate() + (day - current.getDay() + 7) % 7);
                    // While less than end date, add dates to result array
                    while (current < end) {
                        result.push(new Date(+current));
                        current.setDate(current.getDate() + 7);
                    }
                    return result;
                }

                let businessHoursEvents = []

                for (const opening of openingsData) {
                    const workingDays = getDaysBetweenDates(opening.fromDate, opening.toDate, opening.weekday)
                    for (const workDay of workingDays) {
                        let startTime = opening.startTime.split(':').map(Number)
                        let endTime = opening.endTime.split(':').map(Number)

                        let workDayStart = new Date(workDay)
                        workDayStart.setHours(startTime[0], startTime[1], startTime[2])
                        let workDayEnd = new Date(workDay)
                        workDayEnd.setHours(endTime[0], endTime[1], endTime[2])

                        businessHoursEvents.push(
                            {
                                groupId: `workingHours${item.id}`,
                                start: workDayStart.toISOString(),
                                end: workDayEnd.toISOString(),
                                display: 'inverse-background',
                                color: 'grey'
                            }
                        )
                    }
                }



                return {
                    id: item.id,
                    name: item.name,
                    open: openingsData,
                    businessEvents: businessHoursEvents
                }
            }))


            if (test) {
                this.setState({
                    locationsData: test
                })


            }
            this.setState({ clicked: !this.state.clicked })
        } catch (err) {
            console.log(err)
        }
    }

    componentDidUpdate(_prevProps, prevState) {
        if (this.state.currentEvent.id !== prevState.currentEvent.id) {
            this.setState({ showEvent: true })
        }
    }

    deleteConfirmation(eventInfo) {
        if (window.confirm("Jeste li sigurni da želite izbrisati ovaj zahtjev iz kalendara?")) {
            if (localStorage.getItem("IDS") !== null) {
                var storedIDS = JSON.parse(localStorage.getItem("IDS"));
                if (!storedIDS.includes(eventInfo.id)) {
                    storedIDS.push(eventInfo.id)
                    localStorage.setItem("IDS", JSON.stringify(storedIDS))
                }
            } else {
                var IDS = [];
                IDS.push(eventInfo.id)
                localStorage.setItem("IDS", JSON.stringify(IDS));
            }
            this.handleClick()
            this.setState({ showEvent: !this.state.showEvent })
            this.handleRequests(eventInfo.locationID)

        }

    }

    dateFormatter(temp) {
        if (temp.includes("-")) {
            var reversedArr = temp.split("-");
            var reversedArr = reversedArr.reverse()
            var reversed = reversedArr.join("-")
            return reversed;
        } else if (temp.includes(":")) {
            var timeArr = temp.split(":")
            var timeArr = timeArr.slice(0, 2)
            var reversed = timeArr.join(":")
            return reversed
        }
        switch (temp) {
            case "MONDAY":
                return "Ponedjeljak"
            case "TUESDAY":
                return "Utorak"
            case "WEDNESDAY":
                return "Srijeda"
            case "THURSDAY":
                return "Četvrtak"
            case "FRIDAY":
                return "Petak"
            case "SATURDAY":
                return "Subota"
            case "SUNDAY":
                return "Nedjelja"
            default:
                return temp
        }

    }



    render() {

        if (localStorage.getItem('token') === null) {
            return <Redirect to="/" />
        }


        const Test = this.state.locationsData.map(loc =>
            <div key={loc.id} className="location-view">
                <h3>{loc.name}</h3>
                <h3> Otvoreno: </h3>
                {loc.open.map(info =>
                    <div key={info.id}>
                        <h3>{this.dateFormatter(info.weekday)} od {this.dateFormatter(info.fromDate)} u {this.dateFormatter(info.startTime)} do {this.dateFormatter(info.toDate)} u {this.dateFormatter(info.endTime)}. Cijena: {info.cost} kn</h3>
                    </div>
                )}
                <h3 ><button className="show-request" onClick={() => this.handleRequests(loc.id)}>Prikaži zahtjeve</button></h3>
            </div>
        )



        return (
            <div id='calendar'>
                <h2 id='titleC'>Renter Kalendar</h2>
                {this.state.clicked ?
                    <button className="btn-show" onClick={() => this.changeClick()}>Sakrij moje objekte</button>
                    :
                    <button className="btn-show" onClick={() => this.handleClick()}>Prikaži moje objekte</button>
                }
                {this.state.clicked ?
                    Test
                    :
                    ''
                }
                <div className="circleLegend">
                    <div className="redCircle">
                        <i className="fas fa-circle"></i>
                        Potvrđeni zahtjevi
                    </div>
                    <div className="greenCircle">
                        <i class="fas fa-circle"></i>
                        Otvoreni zahtjevi
                    </div>
                </div>
                <FullCalendar locale='hr'
                    plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
                    headerToolbar={{
                        left: 'prev,next today',
                        center: 'title',
                        right: 'dayGridMonth,timeGridWeek,timeGridDay'
                    }}
                    selectable={true}
                    editable={false}
                    buttonText={{
                        today: 'Danas',
                        month: 'Mjesec',
                        week: 'Tjedan',
                        day: 'Dan'
                    }}
                    aspectRatio="2"
                    initialView="dayGridMonth"
                    eventBackgroundColor="white"
                    eventDisplay="block"
                    eventTextColor="black"
                    weekends={this.state.weekendsVisible}
                    events={this.state.eventsData}
                    eventClick={this.handleEventClick}
                />

                <div id={this.state.showEvent ? 'active-event-info' : 'event-info'}>
                    {(this.state.showEvent) ?
                        <>
                            <h3 id="info-title"> Informacije </h3>
                            <h3> {this.state.currentEvent.locationName}</h3>
                            <h3> {this.state.currentEvent.title}</h3>
                            <h3> Vrijeme: Od {this.state.currentEvent.formattedStart} do {this.state.currentEvent.formattedEnd}</h3>
                            <h3> Broj sudionika: {this.state.currentEvent.participants} / {this.state.currentEvent.maxNumberOfParticipants}</h3>
                            <h3> Cijena: {this.state.currentEvent.cost ? this.state.currentEvent.cost : "Besplatno"} </h3>
                            {this.state.currentEvent.isConfirmed ? <h3>Ovaj zahtjev je prihvaćen. <button className="deny-request" onClick={() => this.deleteConfirmation(this.state.currentEvent)}>Izbriši? </button></h3> : <>
                                <h3><button className="accept-request" onClick={() => this.acceptRequest(this.state.currentEvent)}>Prihvati Zahtjev</button></h3>
                                <h3 ><button className="deny-request" onClick={() => this.denyRequest(this.state.currentEvent)}>Odbij Zahtjev</button></h3>
                            </>}
                        </> : ''}
                </div>
            </div >
        )
    }
}

export default RCalendar