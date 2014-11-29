var app = app || {};

$(function(){
	var $gridSystem = $("body").find(".debug-grid-system");
	if($gridSystem.length == 0){
		$("body").append($("<div class='debug-grid-system'><div class='debug-grid-system-canvas'><canvas width='0' height='0'/></div><i class='toggleGrid glyphicon glyphicon-th'/></div>"));
		$gridSystem = $("body").find(".debug-grid-system");
	}

	drawGrid();

	function drawGrid(){
		var $canvas = $gridSystem.find("canvas");
		if($canvas.length == 0){
			return;
		}
		var gtx = Gtx($canvas);
		gtx.fitParent();

		var width = $canvas.width();
		var height = $canvas.height();
		var unitWidth = 8;
		var unitHeight = 8;

		gtx.setStrokeStyle("rgba(221,221,221,.6)");
		gtx.lineWidth(1);
		for(var i = 0; i * unitWidth < width; i++){
			gtx.beginPath();
			gtx.moveTo((i + 1) * unitWidth, 0);
			gtx.lineTo((i + 1) * unitWidth, height);
			gtx.stroke();
		}

		for(var i = 0; i * unitHeight < height; i++){
			gtx.beginPath();
			gtx.moveTo(0, (i + 1) * unitHeight);
			gtx.lineTo(width, (i + 1) * unitHeight);
			gtx.stroke();
		}
	}

	$("body").on("click", ".debug-grid-system .toggleGrid", function(){
		var $canvasDiv = $gridSystem.find(".debug-grid-system-canvas");
		if($canvasDiv.is(":visible")){
			$canvasDiv.hide();
		}else{
			$canvasDiv.show();
			drawGrid();
		}
	});

	$("body").on("click", ".debug-grid-system canvas", function(){
		var $canvasDiv = $gridSystem.find(".debug-grid-system-canvas");
		$canvasDiv.hide();
	});

});