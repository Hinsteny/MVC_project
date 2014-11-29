(function(){

	brite.registerView("ChainView",{

		create: function(data){
			var view = this;
			data = data || {};
			view.chainId = data.chain.id;
			return render("ChainView");
		},

		postDisplay: function(data){
			var view = this; 
			refreshRows.call(view);
		},

		events: {
			"click; .btnToggleEditRow": function(event){
				var view = this;
				var $rowAddBox = $(event.target).closest(".rowAddBox");
				$rowAddBox.find(".rowEditContent").removeClass("hide");
				$rowAddBox.find(".btnToggleEditRow").addClass("hide");

			},
			"click; .rowEditContent .btnAdd": function(event){
				var view = this;
				var $input = $(event.target).closest(".rowEditContent").find(".newRowInput");
				var newRow = {};
				newRow.name = $input.val();
				newRow.chainId = view.chainId;
				brite.dao("Row").create(newRow).done(function(result){
					refreshRows.call(view);
					$input.val("");
					var $rowAddBox = $(event.target).closest(".rowAddBox");
					$rowAddBox.find(".rowEditContent").addClass("hide");
					$rowAddBox.find(".btnToggleEditRow").removeClass("hide");
				});

			},
			"click; .rowEditContent .btnCancel": function(event){
				var view = this;
				var $rowAddBox = $(event.target).closest(".rowAddBox");
				$rowAddBox.find(".rowEditContent").addClass("hide");
				$rowAddBox.find(".btnToggleEditRow").removeClass("hide");
			},

			"click; .row .iconToggleItemEdit":function(event){
				var view = this;
				var $iconToggleItemEdit = $(event.currentTarget)
				var $row = $iconToggleItemEdit.closest(".row");
				if($row.find(".itemEditContent").size() > 0){
					$row.find(".itemEditContent").remove();
				}else{
					var rowId = $row.attr("data-rowId");
					$row.append(render("ChainView-item-edit", {rowId:rowId}));
				}
				$iconToggleItemEdit.toggleClass("active");
			},

			"click; .itemEditContent .btnAdd": function(event){
				var view = this;
				var $itemEditContent = $(event.target).closest(".itemEditContent");
				var $input = $itemEditContent.find(".newItemInput");
				var $row = $itemEditContent.closest(".row");
				var $iconToggleItemEdit = $row.find(".iconToggleItemEdit");
				var newItem = {};
				newItem.name = $input.val();
				newItem.rowId = $itemEditContent.attr("data-rowId");
				brite.dao("Item").create(newItem).done(function(result){
					refreshItemList.call(view, newItem.rowId);
					$itemEditContent.remove();
					$iconToggleItemEdit.removeClass("active");
				});

			},
			"click; .itemEditContent .btnCancel": function(event){
				var view = this;
				var $itemEditContent = $(event.currentTarget).closest(".itemEditContent");
				var $row = $itemEditContent.closest(".row");
				var $iconToggleItemEdit = $row.find(".iconToggleItemEdit");
				$itemEditContent.remove();
				$iconToggleItemEdit.removeClass("active");
			},
		},

	});


	// --------- Private Methods --------- //
	function refreshRows(){
		var view = this;
		var $ul = view.$el.find("ul");
		var $firstLi;
		app.rowDao.list({filter:{chainId:view.chainId}}).done(function(rows){
			var $rowsCon = view.$el.find(".rowsCon").empty();
			for(var i = 0; i < rows.length; i++){
				$rowsCon.append(render("ChainView-rows-rowItem",rows[i]));
				refreshItemList.call(view, rows[i].id);

			}
			
		});
	}

	function refreshItemList(rowId){
		var view = this;
		var $row = view.$el.find(".row[data-rowId='"+rowId+"']");
		var $itemsCon = $row.find(".row-items").empty();
		app.itemDao.list({filter:{rowId:rowId}}).done(function(items){
			for(var i = 0; i < items.length; i++){
				$itemsCon.append(render("ChainView-items-item",items[i]));
			}
		});
	}
	
	// --------- /Private Methods --------- //


})();