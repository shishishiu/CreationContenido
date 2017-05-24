/*
Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
For licensing, see license.txt or http://cksource.com/ckfinder/license
*/

CKFinder.customConfig = function( config )
{
	// Define changes to default configuration here.
	// For the list of available options, check:
	// http://docs.cksource.com/ckfinder_2.x_api/symbols/CKFinder.config.html

	config.callback = function( api ) {
		   // Disable function
		   api.disableFolderContextMenuOption( 'createSubFolder', false );
		   api.disableFolderContextMenuOption( 'renameFolder', false );
		   api.disableFolderContextMenuOption( 'deleteFolder', false );
		   api.disableFileContextMenuOption( 'downloadFile', false );
		   api.disableFileContextMenuOption( 'renameFile', false );         
		   api.disableFileContextMenuOption( 'deleteFile', false ); }
};
