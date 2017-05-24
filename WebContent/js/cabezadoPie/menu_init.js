/* author: Victor Garcia */
/* Creation date: 03/04/2002 */
/***
This is the menu creation code - place it right after you body tag
Feel free to add this to a stand-alone js file and link it to your page.
**/

//Menu object creation
oCMenu=new makeCM("oCMenu") //Making the menu object. Argument: menuname

//Menu properties
oCMenu.pxBetween=0
oCMenu.fromLeft=100
oCMenu.fromTop=130
oCMenu.rows=1
oCMenu.menuPlacement="center"
                                                             
oCMenu.offlineRoot=""
oCMenu.onlineRoot=""
oCMenu.resizeCheck=1
oCMenu.wait=200
oCMenu.fillImg="cm_fill.gif"
oCMenu.zIndex=20


//Background bar properties
oCMenu.useBar=1
oCMenu.barWidth="0"
oCMenu.barHeight="menu"
oCMenu.barClass="clBar"
oCMenu.barX=80
oCMenu.barY=89
oCMenu.barBorderX=0
oCMenu.barBorderY=0
oCMenu.barBorderClass=""
//Background bar properties
//oCMenu.useBar=1
//oCMenu.barWidth="700"
//oCMenu.barHeight="menu"
//oCMenu.barClass="clBar"
//oCMenu.barX=100
//oCMenu.barY=89
//oCMenu.barBorderX=0
//oCMenu.barBorderY=0
//oCMenu.barBorderClass=""

//Level properties - ALL properties have to be spesified in level 0
oCMenu.level[0]=new cm_makeLevel() //Add this for each new level
oCMenu.level[0].width=120
oCMenu.level[0].height=20
oCMenu.level[0].regClass="clLevel0"
oCMenu.level[0].overClass="clLevel0over"
oCMenu.level[0].borderX=1
oCMenu.level[0].borderY=1
oCMenu.level[0].borderClass="clLevel0border"
oCMenu.level[0].offsetX=0
oCMenu.level[0].offsetY=0
oCMenu.level[0].rows=0
oCMenu.level[0].arrow=0
oCMenu.level[0].arrowWidth=0
oCMenu.level[0].arrowHeight=0
oCMenu.level[0].align="bottom"


//EXAMPLE SUB LEVEL[1] PROPERTIES - You have to specify the properties you want different from LEVEL[0] - If you want all items to look the same just remove this
oCMenu.level[1]=new cm_makeLevel() //Add this for each new level (adding one to the number)
oCMenu.level[1].width=160
oCMenu.level[1].height=22
oCMenu.level[1].regClass="clLevel1"
oCMenu.level[1].overClass="clLevel1over"
oCMenu.level[1].borderX=1
oCMenu.level[1].borderY=1
oCMenu.level[1].align="right" 
oCMenu.level[1].offsetX=-(oCMenu.level[0].width-2)/2+20
oCMenu.level[1].offsetY=0
oCMenu.level[1].borderClass="clLevel1border"


//EXAMPLE SUB LEVEL[2] PROPERTIES - You have to spesify the properties you want different from LEVEL[1] OR LEVEL[0] - If you want all items to look the same just remove this
oCMenu.level[2]=new cm_makeLevel() //Add this for each new level (adding one to the number)
oCMenu.level[2].width=160
oCMenu.level[2].height=20
oCMenu.level[2].offsetX=0
oCMenu.level[2].offsetY=0
oCMenu.level[2].regClass="clLevel2"
oCMenu.level[2].overClass="clLevel2over"
oCMenu.level[2].borderClass="clLevel2border"
