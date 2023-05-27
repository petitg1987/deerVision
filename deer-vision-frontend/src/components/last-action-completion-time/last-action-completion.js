import React, {Component} from 'react';
import './last-action-completion.css';
import {getWithToken} from "../../js/request";
import {getBackendUrl} from "../../js/access";

class LastActionCompletion extends Component {

    constructor(props) {
        super(props);
        this.state = {tableData: []};
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

            lastCompletionsData.push(
                <tr key={shortRequestKey}>
                    <td>{lc.creationDateTime}</td>
                    <td>{lc.levelId}</td>
                    <td>{lc.actionName}</td>
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