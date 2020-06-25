package routes

import (
	"natura/controllers"
	"github.com/gin-gonic/gin"
)

//SetupRouter ... Configure routes
// func SetupRouter() *gin.Engine {
// 	r := gin.Default()
// 	grp1 := r.Group("/natura-api")
// 	{ 
// 		grp1.GET("user/:id", controllers.GetUser)
// 		// grp1.GET("authenticate", controllers.Authenticate)
// 		// grp1.POST("register", controllers.RegisterUser)
// 		// grp1.POST("login", controllers.Login)
// 		// grp1.POST("logout", controllers.Logout)
// 		// grp1.PUT("email/:id", controllers.UpdateUserEmail)
// 		// grp1.PUT("password/:id", controllers.UpdateUserPassword)
// 		// grp1.DELETE(":id", controllers.DeleteUser)
// 	}
// 	return r
// }

func SetupRouter() *gin.Engine {
	r := gin.Default()
	grp := r.Group("/natura-api")
	{ 
		grp1 := grp.Group("/user")
		{ 
			grp1.GET(":id", controllers.DispatchGetUser)
			// grp1.GET("authenticate", controllers.Authenticate)
			// grp1.POST("register", controllers.RegisterUser)
			// grp1.POST("login", controllers.Login)
			// grp1.POST("logout", controllers.Logout)
			// grp1.PUT("email/:id", controllers.UpdateUserEmail)
			// grp1.PUT("password/:id", controllers.UpdateUserPassword)
			// grp1.DELETE(":id", controllers.DeleteUser)
		}
		grp2 := grp.Group("/flower")
		{ 
			grp2.POST("new", controllers.CreateFlower)
			grp2.GET(":param1", controllers.DispatchGetFlower)
			grp2.GET(":param1/:param2", controllers.DispatchGetFlower)
		}
		grp3 := grp.Group("/insect")
		{ 
			grp3.POST("new", controllers.CreateInsect)
			grp3.GET(":param1", controllers.DispatchGetInsect)
			grp3.GET(":param1/:param2", controllers.DispatchGetInsect)
		}
		grp4 := grp.Group("/image")
		{ 
			grp4.POST("upload", controllers.Upload)
			grp4.GET("get/:id", controllers.Download)
		}
	}
	return r
}