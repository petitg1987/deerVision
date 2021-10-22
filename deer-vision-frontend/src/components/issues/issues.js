import React, {Component} from 'react';
import './issues.css';
import {deleteWithToken, getWithToken} from "../../js/request";

class Issues extends Component {

    constructor(props) {
        super(props);
        this.state = {tableData: []};
    }

    async deleteDone() {
        await this.refreshIssues();
    }

    async deleteIssue(event, issueId) {
        event.preventDefault();
        deleteWithToken(this.props.backendUrl + 'api/admin/issues/' + issueId, this.props.token).then(() => this.deleteDone());
    }

    async refreshIssues() {
        let issuesJson = await getWithToken(this.props.backendUrl + 'api/admin/issues', this.props.token);

        let issuesData = [];
        issuesJson.forEach(issue => {
            let appName = issue.appId
                .replace(/([A-Z])/g, ' $1') //insert a space before all caps
                .replace(/^./, (str) => str.toUpperCase());
            let shortAppVersion = issue.appVersion
                .replace('snapshot', 'snap');
            let osName = issue.operatingSystem
                .replace('-', ' ')
                .replace(/^./, (str) => str.toUpperCase());
            let shortUserKey = parseInt(issue.userKey
                .replace(/-.*$/, ''))
                .toString(36)
                .toUpperCase()

            issuesData.push(
                <tr key={issue.id}>
                    <td>{issue.dateTime}</td>
                    <td>{appName}</td>
                    <td>{shortAppVersion}</td>
                    <td className="secondaryInfo">{osName}</td>
                    <td className="secondaryInfo">{shortUserKey}</td>
                    <td>View | <a className="text-link" href="/" onClick={evt => this.deleteIssue(evt, issue.id)}>Delete</a></td>
                </tr>
            );
        });

        this.setState({tableData: issuesData});
    }

    async componentDidMount() {
        await this.refreshIssues();
    }

    render() {
        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Name</th>
                            <th>Version</th>
                            <th className="secondaryInfo">OS</th>
                            <th className="secondaryInfo">User key</th>
                            <th>Action</th>
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

export default Issues;