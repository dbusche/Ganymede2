package ganymede.api;

public enum Level {

   /**
    * A severe error that will prevent the application from continuing.
    */
   FATAL("FATAL"),
   
   /**
    * An error in the application, possibly recoverable.
    */
	ERROR("ERROR"),

   /**
    * An event that might possible lead to an error.
    */
   WARN("WARN"),

   /**
    * An event for informational purposes.
    */
   INFO("INFO"),

   /**
    * A general debugging event.
    */
   DEBUG("DEBUG"),

   /**
    * A fine-grained debug message, typically capturing the flow through the application.
    */
   TRACE("TRACE"),

   /**
    * All events should be logged.
    */
   ALL("ALL"),

   UNKNOWN("UNKNOWN"),
   ;

   private final String _name;

   private Level(String name) {
       _name = name;
   }

   public String getName() {
	   return _name;
   }

}
