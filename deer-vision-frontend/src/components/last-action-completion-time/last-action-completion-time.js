import React, {Component} from 'react';
import './last-action-completion-time.css';
import {getWithToken} from "../../js/request";
import {getBackendUrl} from "../../js/access";

class LastActionCompletionTime extends Component {

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
            let levelId = lc.levelId;
            let actionName = lc.actionName;
            let creationDateTime = lc.creationDateTime;

            lastCompletionsData.push(
                <tr key={shortRequestKey}>
                    <td>{creationDateTime}</td>
                    <td>{levelId}</td>
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

export default LastActionCompletionTime;