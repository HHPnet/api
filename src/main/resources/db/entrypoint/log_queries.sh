#!/usr/bin/env bash

sed -i "s/#log_statement = 'none'			# none, ddl, mod, all/log_statement = 'all'/" ${PGDATA?"/var/lib/postgresql/data"}/postgresql.conf