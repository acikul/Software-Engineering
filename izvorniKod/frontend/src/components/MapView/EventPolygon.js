import React from 'react'
import { Polygon } from 'react-leaflet'

class EventPolygon extends React.Component {
    render() {
        return (
            <Polygon 
                positions={this.props.positions}
                onClick = {() => { this.props.events ?
                                    this.props.onMarkerClick(this.props.events) :
                                    this.props.onMarkerClick(this.props.location) }} >
            </Polygon>
        )
    }
}

export default EventPolygon