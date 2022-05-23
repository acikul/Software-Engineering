import axios from 'axios'
import React from 'react'
import { withRouter } from 'react-router-dom'
import { Link } from 'react-router-dom'

class CoachDocumentations extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            documentations: []
        }

        this.componentDidMount = this.componentDidMount.bind(this)
        this.removeDocument = this.removeDocument.bind(this)
        this.rejectDocumentation = this.rejectDocumentation.bind(this)
        this.acceptDocumentatiton = this.acceptDocumentatiton.bind(this)
    }

    async componentDidMount() {
        let data = [...(await axios.get('/v1/documentations/unresolved')).data]
        this.setState({
            documentations: data.map(element => (
                {
                    id: element.id,
                    documentInternUri: element.documentInternUri
                }
            ))
        })
    }

    removeDocument(id) {
        this.setState({
            documentations: this.state.documentations.filter(element => (
                element.id != id
            ))
        })
    }

    async rejectDocumentation(id) {
        this.removeDocument(id)
        await axios.delete('/v1/documentations/' + id)
        alert('Certifikat odbijen!')
    }

    async acceptDocumentatiton(id) {
        this.removeDocument(id)
        await axios.post('/v1/documentations/approve/' + id)
        alert('Certifikat prihvaćen!')
    }

    render() {
        console.log(this.state.documentations)
        let docsArray = null
        if (this.state.documentations) {
            docsArray =
                this.state.documentations.map(element => (
                    <li key={element.id}>
                        <iframe src={'/' + element.documentInternUri} title='Pregled certifikata' />
                        <button onClick={e => this.acceptDocumentatiton(element.id)}>Prihvati certifikat</button>
                        <button onClick={e => this.acceptDocumentatiton(element.id)}>Odbij certifikat</button>
                    </li>
                ))
            console.log(docsArray)
        }
        else {
            docsArray = <h2>Nema više certifikata za obraditi</h2>
        }
        return (
            <ul>
                {docsArray}
            </ul>
        )
    }
}

export default withRouter(CoachDocumentations)  