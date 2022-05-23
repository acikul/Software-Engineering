import React from "react"

class SportCheckBoxes extends React.Component {
    render() {
        return(
            <label>
                <input 
                    type="checkbox"
                    checked={this.props.checked}
                    onChange={this.props.handleChange}
                    value={this.props.value}
                />  {this.props.value} 
            </label>
        )
    }
}

export default SportCheckBoxes