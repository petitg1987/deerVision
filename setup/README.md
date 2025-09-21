# Pre-requisites
* Set up a server using 'serverSetup' project
* Create a file `~/.vault_pass.txt` with the Ansible vault password
* Update the IP address of the server in 'infra/inventory.ini'
* Execute locally: `ansible-playbook -i setup/inventory.ini setup/site.yml --vault-password-file ~/.vault_pass.txt`

# Deploy the application
* Commit on master branch to trigger the GitHub actions
