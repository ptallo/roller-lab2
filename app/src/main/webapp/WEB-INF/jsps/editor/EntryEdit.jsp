<%--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  The ASF licenses this file to You
  under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.  For additional information regarding
  copyright in this work, please see the NOTICE file in the top level
  directory of this distribution.
--%>
<%@ include file="/WEB-INF/jsps/taglibs-struts2.jsp" %>

<%@ page import="org.apache.roller.weblogger.pojos.strategy.StrategyHandler,
org.apache.roller.weblogger.pojos.strategy.TFIDF,
java.util.*" %>

<link rel="stylesheet" media="all" href='<s:url value="/roller-ui/jquery-ui-1.11.0/jquery-ui.min.css"/>' />

<script src="<s:url value="/roller-ui/scripts/jquery-2.1.1.min.js" />"></script>
<script src='<s:url value="/roller-ui/jquery-ui-1.11.0/jquery-ui.min.js"/>'></script>

<style>
#tagAutoCompleteWrapper {
    width:40em; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
</style>

<%-- Titling, processing actions different between entry add and edit --%>
<s:if test="actionName == 'entryEdit'">
    <s:set var="subtitleKey">weblogEdit.subtitle.editEntry</s:set>
    <s:set var="mainAction">entryEdit</s:set>
</s:if>
<s:else>
    <s:set var="subtitleKey">weblogEdit.subtitle.newEntry</s:set>
    <s:set var="mainAction">entryAdd</s:set>
</s:else>

<p class="subtitle">
    <s:text name="%{#subtitleKey}" >
        <s:param value="actionWeblog.handle" />
    </s:text>
</p>

<s:form id="entry">
	<s:hidden name="salt" />
    <s:hidden name="weblog" />
    <s:hidden name="bean.status" />
    <s:if test="actionName == 'entryEdit'">
        <s:hidden name="bean.id" />  <!-- this is the id of the current Weblog -->
    </s:if>

    <%-- ================================================================== --%>
    <%-- Title, category, dates and other metadata --%>

    <table class="entryEditTable" cellpadding="0" cellspacing="0" style="width:100%">

        <tr>
            <td class="entryEditFormLabel">
                <label for="title"><s:text name="weblogEdit.title" /></label>
            </td>
            <td>
                <s:textfield name="bean.title" size="70" maxlength="255" tabindex="1" style="width:60%"/>
            </td>
        </tr>

        <tr>
            <td class="entryEditFormLabel">
                <label for="status"><s:text name="weblogEdit.status" /></label>
            </td>
            <td>
                <s:if test="bean.published">
                    <span style="color:green; font-weight:bold">
                        <s:text name="weblogEdit.published" />
                        (<s:text name="weblogEdit.updateTime" />
                        <s:date name="entry.updateTime" />)
                    </span>
                </s:if>
                <s:elseif test="bean.draft">
                    <span style="color:orange; font-weight:bold">
                        <s:text name="weblogEdit.draft" />
                        (<s:text name="weblogEdit.updateTime" />
                        <s:date name="entry.updateTime" />)
                    </span>
                </s:elseif>
                <s:elseif test="bean.pending">
                    <span style="color:orange; font-weight:bold">
                        <s:text name="weblogEdit.pending" />
                        (<s:text name="weblogEdit.updateTime" />
                        <s:date name="entry.updateTime" />)
                    </span>
                </s:elseif>
                <s:elseif test="bean.scheduled">
                    <span style="color:orange; font-weight:bold">
                        <s:text name="weblogEdit.scheduled" />
                        (<s:text name="weblogEdit.updateTime" />
                        <s:date name="entry.updateTime" />)
                    </span>
                </s:elseif>
                <s:else>
                    <span style="color:red; font-weight:bold"><s:text name="weblogEdit.unsaved" /></span>
                </s:else>
            </td>
        </tr>


        <s:if test="actionName == 'entryEdit'">
            <tr>
                <td class="entryEditFormLabel">
                    <label for="permalink"><s:text name="weblogEdit.permaLink" /></label>
                </td>
                <td>
                    <s:if test="bean.published">
                        <a id="entry_bean_permalink" href='<s:property value="entry.permalink" />'><s:property value="entry.permalink" /></a>
                        <img src='<s:url value="/images/launch-link.png"/>' />
                    </s:if>
                    <s:else>
                        <s:property value="entry.permalink" />
                    </s:else>
                </td>
            </tr>
        </s:if>

        <tr>
            <td class="entryEditFormLabel">
                <label for="categoryId"><s:text name="weblogEdit.category" /></label>
            </td>
            <td>
                <s:select name="bean.categoryId" list="categories" listKey="id" listValue="name" size="1" />
            </td>
        </tr>

        <tr>
            <td class="entryEditFormLabel">
                <label for="title"><s:text name="weblogEdit.tags" /></label>
            </td>
            <td>
                <s:textfield id="tagAutoComplete" cssClass="entryEditTags" name="bean.tagsAsString" size="70" maxlength="255" tabindex="3" style="width:30%"/>
            </td>
        </tr>
        
        <s:if test="actionName == 'entryEdit'">
         	<%
		    	StrategyHandler handler = new StrategyHandler(request.getParameter("bean.id"));
		    	TFIDF strategy = new TFIDF();
		    	HashMap<String, Double> tags = handler.runStrategy(strategy);
		    	ArrayList<String> recomendedTags = new ArrayList<String>();
		    	for(String word : tags.keySet()){
		    		recomendedTags.add(word);
		    	}
		    	
		    	String tag1 = recomendedTags.get(0);
		    	Double tag1Val = tags.get(tag1);
		    	String tag2 = recomendedTags.get(1);
		    	Double tag2Val = tags.get(tag2);
		    	String tag3 = recomendedTags.get(2);
		    	Double tag3Val = tags.get(tag3);
		    %>
	       <tr>
	       	<td class="entryEditFormLabel">
	       		<label for="title"><s:text name="weblogEdit.genTags"/></label>
	       	</td>
	       	<td><%=tag1%> <%=tag2%> <%=tag3%></td>
	       	<td id="infographic">
	       		<!-- Popup Code -->
				<style>
			        .popup {
			            position: relative;
			            display: inline-block;
			            cursor: pointer;
			            -webkit-user-select: none;
			            -moz-user-select: none;
			            -ms-user-select: none;
			            user-select: none;
			        }
			
			        .popup .popupCanvas {
			            visibility: hidden;
			            width: 300px;
			            height: 400px;
			            background-color: #555;
			            color: #fff;
			            text-align: center;
			            border-radius: 6px;
			            padding: 8px 0;
			            position: absolute;
			            z-index: 1;
			            bottom: 125%;
			            left: 50%;
			            top: 50%;
			            margin-left: -80px;
			        } 
			
			        .popup .show {
			            visibility: visible;
			            -webkit-animation: fadeIn 1s;
			            animation: fadeIn 1s;
			        }
			
			        @-webkit-keyframes fadeIn {
			            from {opacity: 0;}
			            to {opacity: 1;}
			        }
			
			        @keyframes fadeIn {
			            from {opacity: 0;}
			            to {opacity:1 ;}
			        }
			    </style>
				    
				<div class="popup" onclick="myFunction()">Click me to see how tags were generated!
				    <span class="popupCanvas" id="myPopup">
				        <canvas id="tagCanvas"></canvas>
				        <legend for="tagCanvas"></legend>
				    </span>
				</div>
				
				<script>
				    function myFunction() {
				        var popup = document.getElementById("myPopup");
				        popup.classList.toggle("show");
				    }
				
				    var tagCanvas = document.getElementById("tagCanvas");
				    tagCanvas.width = 300;
				    tagCanvas.height = 300;
				
				    var ctx = tagCanvas.getContext("2d");
				
				    var exampleTags = {
				    	<%=tag1%> : <%=tag1Val%>,
				    	<%=tag2%> : <%=tag2Val%>,
				    	<%=tag3%> : <%=tag3Val%>
				    };
				    
				
				    function drawLine(ctx, startX, startY, endX, endY,color){
				        ctx.save();
				        ctx.strokeStyle = color;
				        ctx.beginPath();
				        ctx.moveTo(startX,startY);
				        ctx.lineTo(endX,endY);
				        ctx.stroke();
				        ctx.restore();
				    }
				
				    function drawBar(ctx, upperLeftCornerX, upperLeftCornerY, width, height,color){
				        ctx.save();
				        ctx.fillStyle=color;
				        ctx.fillRect(upperLeftCornerX,upperLeftCornerY,width,height);
				        ctx.restore();
				    }
				
				    var Barchart = function(options){
				        this.options = options;
				        this.canvas = options.canvas;
				        this.ctx = this.canvas.getContext("2d");
				        this.colors = options.colors;
				
				        this.draw = function(){
				            var maxValue = 0;
				            for (var categ in this.options.data){
				                maxValue = Math.max(maxValue,this.options.data[categ]);
				            }
				            var canvasActualHeight = this.canvas.height - this.options.padding * 2;
				            var canvasActualWidth = this.canvas.width - this.options.padding * 2;
				
				            var gridValue = 0;
				            while (gridValue <= maxValue){
				                var gridY = canvasActualHeight * (1 - gridValue/maxValue) + this.options.padding;
				                drawLine(
				                    this.ctx,
				                    0,
				                    gridY,
				                    this.canvas.width,
				                    gridY,
				                    this.options.gridColor
				                );
				
				                this.ctx.save();
				                this.ctx.fillStyle = this.options.gridColor;
				                this.ctx.font = "bold 10px Arial";
				                this.ctx.fillText(gridValue, 10,gridY - 2);
				                this.ctx.restore();
				
				                gridValue+=this.options.gridScale;
				            }
				
				            var barIndex = 0;
				            var numberOfBars = Object.keys(this.options.data).length;
				            var barSize = (canvasActualWidth)/numberOfBars;
				
				            for (categ in this.options.data){
				                var val = this.options.data[categ];
				                var barHeight = Math.round( canvasActualHeight * val/maxValue) ;
				                drawBar(
				                    this.ctx,
				                    this.options.padding + barIndex * barSize,
				                    this.canvas.height - barHeight - this.options.padding,
				                    barSize,
				                    barHeight,
				                    this.colors[barIndex%this.colors.length]
				                );
				
				                barIndex++;
				            }
				            this.ctx.save();
				            this.ctx.textBaseline="bottom";
				            this.ctx.textAlign="center";
				            this.ctx.fillStyle = "#000000";
				            this.ctx.font = "bold 14px Arial";
				            this.ctx.fillText(this.options.seriesName, this.canvas.width/2,this.canvas.height);
				            this.ctx.restore();
				
				            barIndex = 0;
				            var legend = document.querySelector("legend[for='tagCanvas']");
				            var ul = document.createElement("ul");
				            legend.append(ul);
				            for (categ in this.options.data){
				                var li = document.createElement("li");
				                li.style.listStyle = "none";
				                li.style.borderLeft = "20px solid "+this.colors[barIndex%this.colors.length];
				                li.style.padding = "5px";
				                li.style.color = "white";
				                li.textContent = categ + " " + this.options.data[categ];
				                ul.append(li);
				                barIndex++;
				            }
				
				        }
				    };
				
				    var myBarchart = new Barchart(
				        {
				            canvas:tagCanvas,
				            seriesName:"TFIDF Values",
				            padding:20,
				            gridScale:0.02,
				            gridColor:"White",
				            data:exampleTags,
				            colors:["#a55ca5","#67b6c7", "#bccd7a"]
				        }
				    );
				
				    myBarchart.draw();
				</script>
	       	</td>
	       </tr>
        </s:if>

        <s:if test="actionWeblog.enableMultiLang">
                <tr> 
                    <td class="entryEditFormLabel">
                        <label for="locale"><s:text name="weblogEdit.locale" /></label>
                    </td>
                    <td>
                        <s:select name="bean.locale" size="1" list="localesList" listValue="displayName" />
                    </td>
                </tr>
            </table>
        </s:if>
        <s:else>
            </table>
            <s:hidden name="bean.locale"/>
        </s:else>


    <%-- ================================================================== --%>
    <%-- Weblog editor --%>

    <s:include value="%{editor.jspPage}" />

    <br />

    <%-- ================================================================== --%>
    <%-- plugin chooser --%>

    <s:if test="!entryPlugins.isEmpty">
        <div id="pluginControlToggle" class="controlToggle">
            <span id="ipluginControl">+</span>
            <a class="controlToggle" onclick="javascript:toggleControl('pluginControlToggle','pluginControl')">
            <s:text name="weblogEdit.pluginsToApply" /></a>
        </div>
        <div id="pluginControl" class="miscControl" style="display:none">
            <s:checkboxlist theme="roller" name="bean.plugins" list="entryPlugins" listKey="name" listValue="name" />
        </div>
    </s:if>


    <%-- ================================================================== --%>
    <%-- advanced settings  --%>

    <div id="miscControlToggle" class="controlToggle">
        <span id="imiscControl">+</span>
        <a class="controlToggle" onclick="javascript:toggleControl('miscControlToggle','miscControl')">
        <s:text name="weblogEdit.miscSettings" /></a>
    </div>
    <div id="miscControl" class="miscControl" style="display:none">

        <label for="link"><s:text name="weblogEdit.pubTime" /></label>
        <div>
            <s:select name="bean.hours" list="hoursList" />
            :
            <s:select name="bean.minutes" list="minutesList" />
            :
            <s:select name="bean.seconds" list="secondsList" />
            &nbsp;&nbsp;
            <script>
            $(function() {
                $( "#entry_bean_dateString" ).datepicker({
                    showOn: "button",
                    buttonImage: "../../images/calendar.png",
                    buttonImageOnly: true,
                    changeMonth: true,
                    changeYear: true
                });
            });
            </script>
            <s:textfield name="bean.dateString" size="12" readonly="true"/>
            <s:property value="actionWeblog.timeZone" />
        </div>
        <br />

        <s:checkbox name="bean.allowComments" />
        <s:text name="weblogEdit.allowComments" />
        <s:text name="weblogEdit.commentDays" />
        <s:select name="bean.commentDays" list="commentDaysList" size="1" listKey="key" listValue="value" />
        <br />

        <s:checkbox name="bean.rightToLeft" />
        <s:text name="weblogEdit.rightToLeft" />
        <br />

        <s:if test="authenticatedUser.hasGlobalPermission('admin')">
            <s:checkbox name="bean.pinnedToMain" />
            <s:text name="weblogEdit.pinnedToMain" /><tags:help key="weblogEdit.pinnedToMain.tooltip"/>
            <br />
        </s:if>
        <br />

		<table>
			<tr>
				<td><s:text name="weblogEdit.searchDescription" />:<tags:help key="weblogEdit.searchDescription.tooltip"/></td>
				<td><s:textfield name="bean.searchDescription" size="60" maxlength="255" style="width:100%"/> </td>
			</tr>
            <tr>
				<td><s:text name="weblogEdit.enclosureURL" />:<tags:help key="weblogEdit.enclosureURL.tooltip"/></td>
				<td><s:textfield name="bean.enclosureURL" size="40" maxlength="255" style="width:100%"/></td>
			</tr>
            <s:if test="actionName == 'entryEdit'">
                <tr>
                    <td></td>
                    <td><s:if test="bean.enclosureURL != null">
                        <s:text name="weblogEdit.enclosureType" />: <s:property value='entry.findEntryAttribute("att_mediacast_type")' />
                        <s:text name="weblogEdit.enclosureLength" />: <s:property value='entry.findEntryAttribute("att_mediacast_length")' />
                    </s:if></td>
                </tr>
            </s:if>
		</table>
    </div>


    <%-- ================================================================== --%>
    <%-- the button box --%>

    <br>
    <div class="control">
        <span style="padding-left:7px">
            <s:submit value="%{getText('weblogEdit.save')}" action="%{#mainAction}!saveDraft" />
            <s:if test="actionName == 'entryEdit'">
                <input type="button" name="fullPreview"
                                    value="<s:text name='weblogEdit.fullPreviewMode' />"
                                    onclick="fullPreviewMode()" />
            </s:if>
            <s:if test="userAnAuthor">
                <s:submit value="%{getText('weblogEdit.post')}" action="%{#mainAction}!publish"/>
            </s:if>
            <s:else>
                <s:submit value="%{getText('weblogEdit.submitForReview')}" action="%{#mainAction}!publish"/>
            </s:else>
        </span>

        <s:if test="actionName == 'entryEdit'">
            <span style="float:right">
                <s:url var="removeUrl" action="entryRemove">
                    <s:param name="weblog" value="actionWeblog.handle" />
                    <s:param name="removeId" value="%{entry.id}" />
                </s:url>
                <input type="button" value="<s:text name='weblogEdit.deleteEntry'/>" onclick="window.location='<s:property value="removeUrl" escape="false" />'" />
            </span>
        </s:if>
    </div>

    
    <%-- ================================================================== --%>
    <%-- Trackback control --%>
    <s:if test="actionName == 'entryEdit' && userAnAuthor">
        <br />
        <h2><s:text name="weblogEdit.trackback" /></h2>
        <s:text name="weblogEdit.trackbackUrl" />
        <br />
        <s:textfield name="trackbackUrl" size="80" maxlength="255" style="width:35%"/>

        <s:submit value="%{getText('weblogEdit.sendTrackback')}" action="entryEdit!trackback" />
    </s:if>

</s:form>

<script>
function fullPreviewMode() {
    window.open('<s:property value="previewURL" />');
}

//Get cookie to determine state of control
if (getCookie('control_miscControl') != null) {
    if(getCookie('control_miscControl') == 'true'){
        toggle('miscControl');
        togglePlusMinus('imiscControl');
    }
}
if (getCookie('control_pluginControl') != null) {
    if(getCookie('control_pluginControl') == 'true'){
        toggle('pluginControl');
        togglePlusMinus('ipluginControl');
    }
}
$(function() {
function split( val ) {
    return val.split( / \s*/ );
}
function extractLast( term ) {
    return split( term ).pop();
}
$( "#tagAutoComplete" )
    // don't navigate away from the field on tab when selecting an item
    .bind( "keydown", function( event ) {
        if ( event.keyCode === $.ui.keyCode.TAB && $( this ).autocomplete( "instance" ).menu.active ) {
            event.preventDefault();
        }
    })
    .autocomplete({
        delay: 500,
        source: function(request, response) {
            $.getJSON("<s:property value='jsonAutocompleteUrl' />", { format: 'json', prefix: extractLast( request.term ) },
            function(data) {
                response($.map(data.tagcounts, function (dataValue) {
                    return {
                        value: dataValue.tag
                    };
                }))
            })
        },
        focus: function() {
            // prevent value inserted on focus
            return false;
        },
        select: function( event, ui ) {
            var terms = split( this.value );
            // remove the current input
            terms.pop();
            // add the selected item
            terms.push( ui.item.value );
            // add placeholder to get the space at the end
            terms.push( "" );
            this.value = terms.join( " " );
            return false;
        }
    });
});
</script>
