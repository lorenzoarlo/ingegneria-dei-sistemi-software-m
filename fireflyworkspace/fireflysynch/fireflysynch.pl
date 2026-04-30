%====================================================================================
% fireflysynch description   
%====================================================================================
dispatch( cellstate, cellstate(X,Y,COLOR) ). %commute cell state on grid
event( sync, sync(TIMER) ). %shared frequency from orchestrator
%====================================================================================
context(ctxfireflysynch, "localhost",  "TCP", "8040").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "external").
  qactor( orchestrator, ctxfireflysynch, "it.unibo.orchestrator.Orchestrator").
 static(orchestrator).
  qactor( firefly, ctxfireflysynch, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
