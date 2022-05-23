import React from 'react'
import axios from 'axios'
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import { Link, Redirect } from 'react-router-dom'
import './Calendar.css'
import CalendarService from '../authServices/CalendarService'



class Calendar extends React.Component {

    constructor() {
        super()

        this.state = {
            weekendsVisible: true,
            currentEvent: '',
            showEvent: false,
            eventsData: [],
            userEventStatus: false
        }

        this.handleClick = this.handleClick.bind(this)
        this.removeFromEvent = this.removeFromEvent.bind(this)

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

    async componentDidMount() {
        const events = await CalendarService()
        if (events) {
            this.setState({ eventsData: events })
        }
        //else {
        //this.setState({ eventsData: this.INITIAL_EVENTS })
        //}

    }


    handleClick = (info) => {
        this.setState({ showEvent: !this.state.showEvent })
        this.setState({ currentEvent: info.event.toPlainObject({ collapseExtendedProps: true }) })

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
                top: 500,
                behavior: 'smooth'
            });
        }

    }

    componentDidUpdate(_prevProps, prevState) {
        if (this.state.currentEvent.id !== prevState.currentEvent.id) {
            this.setState({ showEvent: true })

        }

    }

    async removeFromEvent() {
        try {
            if (window.confirm("Jesi li siguran/sigurna da se želiš odjaviti s ovog sportskog okupljanja?")) {
                const userEventStatus = await axios.delete(`/v1/users/${this.props.user.user_info.id}
                /sportevents/participate/${this.state.currentEvent.id}`)
                if (userEventStatus) {
                    this.setState({ userEventStatus: false })
                    alert("Uspješno si se odjavio/odjavila")
                    window.location.reload()
                } else {
                    alert("Došlo je do pogreške, nisi odjavljen(a)")
                }
            }
        } catch (err) {
            alert("Došlo je do pogreške")
            console.log(err)
        }
    }




    render() {

        if (localStorage.getItem('token') === null) {
            return <Redirect to="/" />
        }

        return (
            <div id='calendar'>
                <h2 id='titleC'>Moj Kalendar</h2>
                <div className="circleLegend">
                    <div className="redCircle">
                        <i className="fas fa-circle"></i>
                        Plaćeno
                    </div>
                    <div className="greenCircle">
                        <i class="fas fa-circle"></i>
                        Besplatno
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
                    eventClick={this.handleClick}
                />
                <div id={this.state.showEvent ? 'active-event-info' : 'event-info'}>
                    {(this.state.showEvent) ?
                        <>
                            <h3 id="info-title"> Informacije </h3>
                            <h3> {this.state.currentEvent.title}</h3>
                            <h3> Vrijeme: Od {this.state.currentEvent.formattedStart} do {this.state.currentEvent.formattedEnd}</h3>
                            <h3> Broj sudionika: {this.state.currentEvent.participants} / {this.state.currentEvent.maxNumberOfParticipants}</h3>
                            <h3> Cijena: {this.state.currentEvent.cost ? this.state.currentEvent.cost : "Besplatno"} </h3>
                            <h3><Link to="/karta"> Lokacija </Link></h3>
                            <h3><a onClick={() => this.removeFromEvent()}>Odjava</a></h3>
                        </> : ''}
                </div>
            </div >
        )

    }
}

export default Calendar;