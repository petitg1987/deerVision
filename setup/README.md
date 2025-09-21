# Pre-requisites
* Set up a server using 'serverSetup' project
* Create a file `~/.vault_pass.txt` with the Ansible vault password
* Update the IP address of the server in 'infra/inventory.ini'
* Execute: `ansible-playbook -i setup/inventory.ini setup/site.yml --vault-password-file ~/.vault_pass.txt`

# Load database from a backup dump
* Unzip backup file 'deervision_dump.sql' in home folder
* Execute: `scp deervision_dump.sql greg@152.53.144.87:/tmp/deervision_dump_to_restore.sql`
* Execute: `ssh greg@152.53.144.87 "cat /tmp/deervision_dump_to_restore.sql | sudo docker exec -i deervision-db psql -U postgres -d postgres"`
* Check: `ssh greg@152.53.144.87 "sudo docker exec -i deervision-db psql -U postgres -d postgres -c 'SELECT * FROM usage ORDER BY id DESC LIMIT 1;'"`

# Connect to the server
* Execute: `ssh greg@152.53.144.87`

# Deploy the application
* Commit on master branch to trigger the GitHub actions
