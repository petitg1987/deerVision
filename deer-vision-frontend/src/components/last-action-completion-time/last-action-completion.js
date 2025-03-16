import React, {Component} from 'react';
import './last-action-completion.css';
import {getWithToken} from "../../js/request";
import {getBackendUrl} from "../../js/access";

class LastActionCompletion extends Component {

    constructor(props) {
        super(props);
        this.state = {tableData: []};
    }

    /*
     * @param dateStr format: "dd/mm/yyyy HH24:MI"
     */
    convertUtcToLocal(dateStr) {
        // Extract date components from "dd/mm/yyyy HH24:MI" format
        let [datePart, timePart] = dateStr.split(' ');
        let [day, month, year] = datePart.split('/').map(Number);
        let [hour, minute] = timePart.split(':').map(Number);

        let utcDate = new Date(Date.UTC(year, month - 1, day, hour, minute));
        let localDate = new Date(utcDate);

        // Format the output as "dd/mm/yyyy HH24:MI"
        let dd = String(localDate.getDate()).padStart(2, '0');
        let mm = String(localDate.getMonth() + 1).padStart(2, '0'); // Month is zero-based
        let yyyy = localDate.getFullYear();
        let HH = String(localDate.getHours()).padStart(2, '0');
        let MI = String(localDate.getMinutes()).padStart(2, '0');

        return `${dd}/${mm}/${yyyy} ${HH}:${MI}`;
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
                    <td>{lc.creationDateTime}</td>
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