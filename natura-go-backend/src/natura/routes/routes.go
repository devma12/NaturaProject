package routes

import (
	"natura/controllers"
	"github.com/gin-gonic/gin"
)

func SetupRouter() *gin.Engine {
	r := gin.Default()
	grp := r.Group("/natura-api")
	{ 
		userGroup := grp.Group("/user")
		{ 
			userGroup.GET(":id", controllers.DispatchGetUser)
			userGroup.POST("register", controllers.RegisterUser)
			userGroup.POST("login", controllers.Login)
			userGroup.POST("logout", controllers.Logout)
			userGroup.PUT("email/:id", controllers.UpdateUserEmail)
			userGroup.PUT("password/:id", controllers.UpdateUserPassword)
			userGroup.DELETE(":id", controllers.DeleteUser)
		}
		flowerGroup := grp.Group("/flower")
		{ 
			flowerGroup.POST("new", controllers.CreateFlower)
			flowerGroup.GET(":param1", controllers.DispatchGetFlower)
			flowerGroup.GET(":param1/:param2", controllers.DispatchGetFlower)
		}
		insectGroup := grp.Group("/insect")
		{ 
			insectGroup.POST("new", controllers.CreateInsect)
			insectGroup.GET(":param1", controllers.DispatchGetInsect)
			insectGroup.GET(":param1/:param2", controllers.DispatchGetInsect)
		}
		imageGroup := grp.Group("/image")
		{ 
			imageGroup.POST("upload", controllers.Upload)
			imageGroup.GET("get/:id", controllers.Download)
		}
		speciesGroup := grp.Group("/species")
		{ 
			speciesGroup.GET("all", controllers.GetAllSpecies)
			speciesGroup.GET("type/:type", controllers.GetSpeciesByType)
			speciesGroup.POST("new", controllers.CreateSpecies)
		}
	}
	return r
}