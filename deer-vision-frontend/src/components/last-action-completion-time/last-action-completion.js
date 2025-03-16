import React, {Component} from 'react';
import './last-action-completion.css';
import {getWithToken} from "../../js/request";
import {getBackendUrl} from "../../js/access";

class LastActionCompletion extends Component {

    constructor(props) {
        super(props);
        this.state = {tableData: []};
    }

    convertUTCToLocal(utcDateString) {
        let formattedUtcString = utcDateString.replace(" ", "T") + "Z";
        let date = new Date(formattedUtcString);

        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, "0");
        let day = String(date.getDate()).padStart(2, "0");
        let hours = String(date.getHours()).padStart(2, "0");
        let minutes = String(date.getMinutes()).padStart(2, "0");

        return `${day}/${month}/${year} ${hours}:${minutes}`;
    }

    async refreshLastCompletions() {
        let lastCompletionsJson = await getWithToken(getBackendUrl() + 'api/admin/levels/lastCompletions?appId=' + this.props.appId, this.props.token);

        let lastCompletionsData = [];
        lastCompletionsJson.forEach(lc => {
            let shortRequestKey = parseInt(lc.requestKey
                .replace(/-.*$/, ''))
                .toString(36)
                .substring(0, 8)
                .toUpperCase();
            let actionName = lc.actionName.replace(/([A-Z])/g, ' $1').trim();

            lastCompletionsData.push(
                <tr key={shortRequestKey}>
                    <td>{this.convertUTCToLocal(lc.creationDateTime)}</td>
                    <td>{lc.levelId}</td>
                    <td>{actionName}</td>
                    <td className="secondary-info">{shortRequestKey}</td>
                </tr>
            );
        });

        this.setState({tableData: lastCompletionsData});
    }

    async componentDidMount() {
        await this.refreshLastCompletions();
    }

    render() {
        return (
            <div>
                <table>
                    <thead>
                    <tr>
                        <th>Date</th>
                        <th>Level</th>
                        <th>Action Name</th>
                        <th className="secondary-info">Key</th>
                    </tr>
                    </thead>
                    <tbody>
                        {this.state.tableData}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default LastActionCompletion;