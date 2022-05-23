import React from 'react'
import axios from 'axios'

import {Map, TileLayer, Marker, Polygon } from 'react-leaflet'


class LocationBox extends React.Component {
    constructor() {
        super()
        this.state = {
            openings: [],
            editOpenings: false,
            editDocumentation: false,
            center: { lat: 45.815, lng: 15.981 }, //lokacija Zagreba
            gpxCoordinates: [],
            startDate:"2021-01-01",
            endDate:"2021-01-01",
            startTime:"00:00",
            endTime:"00:00",
            cost:null,
            weekdays: {
                "MONDAY":false,
                "TUESDAY":false, 
                "WEDNESDAY":false,
                "FRIDAY":false,
                "THURSDAY":false,
                "SATURDAY":false,
                "SUNDAY":false,
            },
            selectedFile: null,
            selectedSports: [],
        }
        this.editWindow = this.editWindow.bind(this)
        this.deleteOpening = this.deleteOpening.bind(this)
        this.addOpening = this.addOpening.bind(this)
        this.handleChange = this.handleChange.bind(this)
    }

    async componentDidMount() {
        console.log(this.props.location)
        const newGPXCoordinates = this.props.location.gpxCoordinates.split("").filter(c => {return c!=="[" && c!=="]"}).join("").split(",")
        let center = {lat:newGPXCoordinates[0], lng:newGPXCoordinates[1]}
        let gpxCoordinates = []
        for (let i = 0; i<newGPXCoordinates.length; i=i+2) {
            gpxCoordinates.push([newGPXCoordinates[i],newGPXCoordinates[i+1]])
        }
        this.setState({center:center, gpxCoordinates:gpxCoordinates})

        try {
            let response = await axios.get(`/v1/users/${this.props.user.user_info.id}/locations/${this.props.location.id}/openings`)
            console.log(response.data)
            this.setState({openings:response.data})
        } catch(err) {
            console.log(err)
        }
    }

    editWindow(bool) {
        this.setState({editOpenings:bool, editDocumentation:!bool})
    }


    async deleteOpening(id) {
        try {
            if(window.confirm("Jesi li siguran/sigurna da želiš obrisati ovo radno vrijeme?")) {
                let response = await axios.delete(`/v1/users/${this.props.user.user_info.id}/locations/${this.props.location.id}/openings/${id}`)
                this.setState(prevState => {
                    let newOpenings = prevState.openings
                    const openings = newOpenings.filter(opening => opening.id !== id)
                    console.log(openings)
                    return {openings:openings}
                })
                console.log(response.data)
            }
        } catch(err) {
            console.log(err)
        }
    }

    async addOpening() {
        let openings = []
        for (const [key, value] of Object.entries(this.state.weekdays)) {
            if (value === true) {
                const opening = {
                    fromDate: this.state.startDate + "T00:00:00",
                    toDate: this.state.endDate + "T00:00:00",
                    startTime: this.state.startTime + ":00",
                    endTime: this.state.endTime + ":00",
                    weekday: key,
                    cost: this.state.cost,
                }
                openings.push(opening)
            }
        }
        for (let i = 0; i<openings.length; i++) {
            try {
                console.log(openings[i])
                let response = await axios.post(`/v1/users/${this.props.user.user_info.id}/locations/${this.props.location.id}/openings`,openings[i])
                console.log(response)
            } catch(err) {
                console.log(err)
            }
        }
        alert("Radno vrijeme uspješno dodano")

    }

    handleChange(event) {
        const { type, value, checked, name } = event.target
        console.log(value)
        if (type === "checkbox") {
            this.setState(prevState => {
                const weekdays = prevState.weekdays
                weekdays[value] = checked
                return {weekdays:weekdays}
            })
        } else {
            this.setState({[name]:value})
        }
    }

    async submitFile() {
        console.log(this.state.selectedSports)
        try {
            const formData = new FormData()
            formData.append('file', this.state.selectedFile)
            const blob = new Blob([JSON.stringify({ "sportsIds": this.state.selectedSports.map(function (item) { return parseInt(item, 10); }) })], {
                type: 'application/json'
            });
            formData.append("documentation_info", blob);
            let response = await axios.post(`/v1/users/${this.props.user.user_info.id}/locations/${this.props.location.id}/documentations`, formData, {
                headers: {
                    'Content-type': 'multipart/form-data'
                }
            })
            console.log(response.data)
            alert('Dokumentacija predana')
        } catch(err) {
            console.log(err)
        }
    }

    render() {
        
        const openings = this.state.openings.map(opening => 
            (<div key={opening.id}>
                    <p>Od {opening.fromDate} do {opening.toDate}</p> 
                    <p>{opening.weekday}: {opening.startTime} - {opening.endTime}</p>
                    <p>Cijena po satu(HRK): {opening.cost}</p>
                    <p className="text-button"
                        onClick={() => this.deleteOpening(opening.id)}>Izbriši radno vrijeme</p>
                    <br/>
            </div>)
        )
        const weekdaysCheckBoxes = ["MONDAY", "TUESDAY", "WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"].map(weekday => 
                        (<label key={weekday}>
                            <input 
                                type="checkbox"
                                checked={this.state.weekdays[weekday]}
                                onChange={this.handleChange}
                                value={weekday}
                            />  {weekday}
                            <br/>
                        </label>)
        )
        const locationMarker = this.state.gpxCoordinates.length === 1 ? 
                    <Marker
                        position={this.state.gpxCoordinates[0]}
                    /> :
                    <Polygon 
                        positions={this.state.gpxCoordinates}
                    />
        return(
            <div className="sport-event-box">
                <div>
                    <h3>{this.props.location.name}</h3>
                    <p>Tip lokacije: {this.props.location.locationStringType === 'free' ? "besplatna" : "plaćena"}</p>
                    {this.props.location.locationStringType === 'paid' ? 
                                    (<div>
                                        <h4>Radno vrijeme:</h4>
                                        {openings}
                                        <p className="text-button"
                                            onClick={() => this.editWindow(true)}
                                        >Dodaj novo radno vrijeme</p>
                                    </div>) 
                                    : null }
                </div>
                <div>
                    {<Map center={this.state.center} zoom={16}>
                        <TileLayer
                            attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors |
                                                Icons made by <a href="https://www.flaticon.com/authors/icongeek26" title="Icongeek26">Icongeek26 </a> 
                                                from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>'
                                                    
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />
                        {locationMarker}
                    </Map>}
                </div>
                <div className="location-options">
                    <div>
                        <p className="text-button" onClick={() => this.props.deleteLocation(this.props.location.id)}>Izbriši ovu lokaciju</p>
                    </div>
                    <div>
                        <p className="text-button" onClick={() => this.editWindow(false)}>Dodaj potvrde za svoju lokaciju</p>
                    </div>
                </div>
                {this.state.editOpenings === true ?
                    (<div>
                        <h2>Odaberite radno vrijeme</h2>
                        <label> Od:
                            <input value={this.state.startDate} type="date" name="startDate" onChange={this.handleChange}/>
                        </label>
                        <br/>
                        <label> Do:
                            <input value={this.state.endDate} type="date" name="endDate" onChange={this.handleChange}/>
                        </label>
                        <br/> 
                            <input value={this.state.startTime} type="time" name="startTime" onChange={this.handleChange}/>
                            - <input value={this.state.endTime} type="time" name="endTime" onChange={this.handleChange}/>
                        <br/>
                        <label> Cijena:
                            <input type="number" name="cost" onChange={this.handleChange}/>
                        </label>
                        <br/>
                        {weekdaysCheckBoxes}
                        <p className="text-button" onClick={() => this.addOpening()}>Završi</p>
                    </div>) 
                    : null}
                {this.state.editDocumentation === true ?
                    (<div>
                        <h2>Odaberite sportove i certifikat:</h2>
                        {this.props.sports.map(element => (
                            <div key={element.id}>
                                <input type='checkbox' value={element.id} name='sport' onChange={e => this.setState({ selectedSports: [...this.state.selectedSports, e.target.value] })} />
                                <label>{element.name}</label>
                            </div>
                        ))}
                        <input type='file' onChange={e => this.setState({ selectedFile: e.target.files[0] })} />
                        <p onClick={() => this.submitFile()} className="text-button">Submit</p>
                    </div>) 
                    : null }
            </div>
        )
    }

}

export default LocationBox