import React, {Component} from 'react';
import './issues.css';
import {deleteWithToken, getWithToken} from "../../js/request";

class Issues extends Component {

    constructor(props) {
        super(props);
        this.state = {tableData: [], issueError: ''};
    }

    async seeIssue(event, issueId) {
        event.preventDefault();
        let issuesJson = await getWithToken(this.props.backendUrl + 'api/admin/issues/' + issueId, this.props.token);
        let htmlValue = issuesJson.value
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;")
            .replaceAll('\n', '<br />')
            .replaceAll('\t', '&nbsp;&nbsp;&nbsp;&nbsp;');
        this.setState({issueError: htmlValue});
        document.querySelector(".popup-background").classList.add("popup-visible");
        document.querySelector(".popup-background").classList.remove("popup-hide");
    }

    async deleteIssue(event, issueId) {
        event.preventDefault();
        deleteWithToken(this.props.backendUrl + 'api/admin/issues/' + issueId, this.props.token)
            .then(async () => {await this.refreshIssues()});
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
                    <td className="secondary-info">{osName}</td>
                    <td className="secondary-info">{shortUserKey}</td>
                    <td><a className="text-link" href="/" onClick={evt => this.seeIssue(evt, issue.id)}>See</a> | <a className="text-link" href="/" onClick={evt => this.deleteIssue(evt, issue.id)}>Delete</a></td>
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
                            <th className="secondary-info">OS</th>
                            <th className="secondary-info">User key</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.tableData}
                    </tbody>
                </table>

                <div className="popup-background popup-hide">
                    <div className="popup-locator"/>
                    <div className="popup">
                        <div dangerouslySetInnerHTML={{__html: this.state.issueError}}></div>
                    </div>
                </div>

            </div>
        );
    }
}

export default Issues;