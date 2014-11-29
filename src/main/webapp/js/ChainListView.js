(function(){

	brite.registerView("ChainListView",{emptyParent:true},{

		create: function(){
			return app.chainDao.list({filter: {orderBy:"name"}}).pipe(function(result){
				return render("ChainListView",{chains:result});
			});
		},

		postDisplay: function(){
			var view = this;
			view.$addNewChain = view.$el.find(".add-new-chain");
			view.$addNewChainLabel = view.$addNewChain.find(".add-new-label");
			view.$addNewChainInput = view.$addNewChain.find(".add-new-input");
		},

		events: {
			"click; .add-new-chain .add-new-label": function(event){
				var view = this;
				var $e = view.$el;
				view.$addNewChainLabel.hide();
				view.$addNewChainInput.show();
				view.$addNewChainInput.find("input").focus();
			},

			"click; .add-new-chain .btnAddChain": function(event){
				var view = this;
				var $e = view.$el;
				var $input = view.$addNewChainInput.find("input");
				var newChain = {name: $input.val()};
				app.chainDao.create(newChain).done(function(chainCreated){
					$input.val("");
					view.$addNewChainLabel.show();
					view.$addNewChainInput.hide();
					refreshList.call(view);
				});
			},

			"click; .add-new-chain .btnCancel": function(event){
				var view = this;
				var $e = view.$el;
				view.$addNewChainInput.find("input").val("");
				view.$addNewChainLabel.show();
				view.$addNewChainInput.hide();
			},

			"keyup; .add-new-chain input": function(event){
				var view = this;
				var $input = $(event.target);
				var key = event.which;
				// press enter
				if (key === 13){
					var newChain = {name: $input.val()};
					$input.val("");
					app.chainDao.create(newChain).done(function(chainCreated){
						// window.location = "#chain/" + chainCreated.id;
						// view.$addNewChain.removeClass("active");
						$input.val("");
						view.$addNewChainLabel.show();
						view.$addNewChainInput.hide();
						refreshList.call(view);
					});
				}
				// press esc
				else if (key === 27){
					$input.val("");
					view.$addNewChainLabel.show();
					view.$addNewChainInput.hide();
				}
			},

			"click; .chain-box .chainName": function(event){
				var view = this;
				var $e = view.$el;
				var $chainName = $(event.target).closest('.chain-box');
				var chainName = $chainName.attr("data-entity-name");
				var chainId = $chainName.attr("data-entity-id");
				var $topTitle = $e.closest(".MainView").find('.navbar-header .navbar-brand');
				$topTitle.append(" > " + chainName)
			}
		},

		daoEvents: {
			"dataChange; Chain": refreshList
		},

		docEvents: {

		}

	});	

	// --------- Private Methods --------- //
	function refreshList(){
		var view = this;
		var $e = view.$el;
		brite.display("ChainListView",$e.closest(".MainView").find(".MainView-leftPanel"));
	}
	// --------- /Private Methods --------- //

})();