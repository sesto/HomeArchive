Exec { path => [ "/bin/", "/sbin/" , "/usr/bin/", "/usr/sbin/" ] }

########### Repository management ###########
class { 'apt':
  apt_update_frequency => weekly
}

################### Java ####################
class { 'java':
  distribution => 'jre',	# defaults to jdk
}

############### Elasticsearch ###############
class { 'elasticsearch':
  version => '1.1.1',
  manage_repo  => true,
  repo_version => '1.1',	# corresponds with the major version of Elasticsearch

}

elasticsearch::plugin{'mobz/elasticsearch-head':
  module_dir => 'mobz',
  instances  => 'ord-01',
 
}

elasticsearch::plugin{ 'elasticsearch/elasticsearch-mapper-attachments/2.0.0':
  module_dir => 'elasticsearch-mapper-attachments',
  instances  => 'ord-01',
 
}

elasticsearch::plugin{ 'com.github.richardwilly98.elasticsearch/elasticsearch-river-mongodb/2.0.0':
  module_dir => 'elasticsearch-river-mongodb',
  instances  => 'ord-01',
  
}


elasticsearch::instance { 'ord-01': }



################## MongoDB ##################
 class { 'mongodb':
    package_name  => 'mongodb-org',
    logdir       => '/var/log/mongodb/',
    # only debian like distros
    old_servicename => 'mongod'
  }
  mongodb::mongod {
    'mongod_home_archive':
      mongod_instance    => 'mongodb1',
      mongod_replSet     => 'rs0',
      mongod_add_options => ['slowms = 50']
  }

  exec { 'initialize_replica_set':
    command => "sudo mongo --eval 'rs.initiate()'",
  	require => Class['mongodb'], 
  
}

##################### TomCat #######################

class { 'tomcat': }
	tomcat::instance { 'tomcat1':
  	source_url => 'http://www.carfab.com/apachesoftware/tomcat/tomcat-7/v7.0.57/bin/apache-tomcat-7.0.57.tar.gz'
}->
tomcat::service { 'default': 

}

################## Deploy war ######################
tomcat::war { 'homearchive.war':
        catalina_base => '/opt/apache-tomcat/',
       	war_source => '/vagrant/target/homearchive.war',
        require => Exec['initialize_replica_set']
     }

