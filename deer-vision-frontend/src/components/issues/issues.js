import React, {Component} from 'react';
import './issues.css';
import {deleteWithToken, getWithToken} from "../../js/request";
import seeIcon from "../../images/seeIcon.webp";
import deleteIcon from "../../images/deleteIcon.webp";

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
            .replaceAll('  ', '&nbsp;&nbsp;')
            .replaceAll('\t', '&nbsp;&nbsp;&nbsp;&nbsp;');
        this.setState({issueError: htmlValue});
        document.querySelector(".popup-background").classList.remove("popup-hide");
    }

    closeIssue(event) {
        event.preventDefault();
        this.setState({issueError: ''});
        document.querySelector(".popup-background").classList.add("popup-hide");
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
            let shortRequestKey = parseInt(issue.requestKey
                .replace(/-.*$/, ''))
                .toString(36)
                .toUpperCase()

            issuesData.push(
                <tr key={issue.id}>
                    <td>{issue.dateTime}</td>
                    <td>{appName}</td>
                    <td>{shortAppVersion}</td>
                    <td className="secondary-info">{osName}</td>
                    <td className="secondary-info">{shortRequestKey}</td>
                    <td>
                        <div className="issues-actions">
                            <div className="issues-action">
                                <a className="text-link" href="/" title="See" onClick={evt => this.seeIssue(evt, issue.id)}>
                                    <img src={seeIcon} alt="See Icon" width="20" height="20"/>
                                </a>
                            </div>
                            <div className="issues-action">
                                <a className="text-link" href="/" title="Delete" onClick={evt => this.deleteIssue(evt, issue.id)}>
                                    <img src={deleteIcon} alt="Delete Icon" width="20" height="20"/>
                                </a>
                            </div>
                        </div>
                    </td>
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
                            <th colSpan={6}>Issues</th>
                        </tr>
                        <tr>
                            <th>Date</th>
                            <th>Application</th>
                            <th>Version</th>
                            <th className="secondary-info">OS</th>
                            <th className="secondary-info">Key</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.tableData}
                    </tbody>
                </table>

                <div className="popup-background popup-hide">
                    <div className="popup-locator"/>
                    <div className="popup">
                        <div dangerouslySetInnerHTML={{__html: this.state.issueError}}/>
                    </div>
                    <div className="popup-close">
                        <a className="text-link" href="/" onClick={evt => this.closeIssue(evt)}>Close</a>
                    </div>
                </div>
            </div>
        );
    }
}

export default Issues;