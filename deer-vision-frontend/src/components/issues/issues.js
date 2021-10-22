import React, {Component} from 'react';
import './issues.css';
import {getWithToken} from "../../js/request";

class Issues extends Component {

    async componentDidMount() {
        let issuesJson = await getWithToken(this.props.backendUrl + 'api/admin/issues', this.props.token);
        console.log(issuesJson);
    }

    render() {
        return (
            <div>

            </div>
        );
    }
}

export default Issues;