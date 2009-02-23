<?php
	//-------------------------------------------------------------------------
	// CONFIG FILE FOR RFIA
	//-------------------------------------------------------------------------	
    $config = Config::getInstance();
    $rootPath = dirname(dirname(__FILE__));
    
    $config->set('dataControllersDir', $rootPath . '/modules/data/controllers/');
    $config->set('dataModelsDir', $rootPath . '/modules/data/models/');

    // Retrieval module
    $config->set('magpierssDir', $rootPath . '/lib/external/magpierss/');
    $config->set('rssURL', 'http://localhost/lottery/rss');
    
    $config->set('rootDir', $rootPath );
    
    $config->set('dbhost', 'localhost');
    $config->set('dbname', 'loteria');
    $config->set('dbuser', 'root');
    $config->set('dbpass', 'root');
    
    $config->set('debug', true);
		