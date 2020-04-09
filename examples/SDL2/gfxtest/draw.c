#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <SDL2/SDL.h>
#include <SDL2/SDL2_gfxPrimitives.h>


static void draw(SDL_Renderer *renderer, SDL_DisplayMode *mode) {
    
    hlineColor(renderer, 0, mode->w, 500, 0xFFFF00FF);
    vlineColor(renderer, mode->w / 2, 0, mode->h, 0xFF00FF00);
    
    thickLineColor(renderer, 100, 600, mode->w - 100, 600, 10, 0xFF00FFFF) ;

	rectangleColor(renderer, 100, 800, 500, 1000, 0xFF0000FF);

	roundedRectangleColor(renderer, 200, 600, 600, 900, 50, 0xFF00BB00);
    
    boxColor(renderer, 300, 1000, 800, 1300, 0xFF0000FF);

	aalineColor(renderer, 0, 500, mode->w, 1800, 0xFFF57C00);


	circleColor(renderer, 800, 1500, 200, 0xFF001AF5);

	ellipseColor(renderer, 0, 300, 500, 600, 0xFF061065);

	filledCircleColor(renderer, 300, 500, 100, 0xFF652D06);
}


int main() {
	// Init sdl
	if(SDL_Init(SDL_INIT_VIDEO | SDL_INIT_EVENTS) < 0)
		goto sdl_error;

	SDL_Window *window = NULL;
	SDL_Surface *surface = NULL;
	SDL_Renderer *renderer = NULL;
	SDL_Texture *texture = NULL;
	SDL_DisplayMode mode;

	SDL_BlendMode blendMode = SDL_BLENDMODE_NONE;

	SDL_GetDesktopDisplayMode(0, &mode);
	// create window 
	window = SDL_CreateWindow("SDL Draw",
			 SDL_WINDOWPOS_CENTERED,
			 SDL_WINDOWPOS_CENTERED,
			 mode.w,
			 mode.h,
			 SDL_WINDOW_SHOWN | SDL_WINDOW_OPENGL);
	
	if(!window) goto sdl_error;
	
	renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);
	if(!renderer) goto sdl_error;
	
	
	surface = SDL_CreateRGBSurface(0, mode.w, mode.h, 32, 0, 0, 0, 0);

	if(!surface) goto sdl_error;
	SDL_FillRect(surface, NULL, 0xFFFFFF);
    
	texture = SDL_CreateTextureFromSurface(renderer, surface);
	if(!texture) goto sdl_error;

	SDL_FreeSurface(surface);
	SDL_SetRenderDrawBlendMode(renderer, blendMode);
	//SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
	SDL_RenderClear(renderer);
	// 绘制texture
	SDL_RenderCopy(renderer, texture, NULL, NULL);
	// 绘制图形(放在绘制texture之后)
	draw(renderer, &mode);
	SDL_RenderPresent(renderer);

	bool quit = false;
	SDL_Event event;
	
	while(!quit){

		while(SDL_WaitEvent(&event)) {
		
		    switch(event.type) {
		    case SDLK_q:
		    case SDL_QUIT:
			    quit = true;
			    break;
		    }
		}
		
		SDL_Delay(10);
	}

	SDL_DestroyWindow(window);
	SDL_DestroyRenderer(renderer);
	SDL_DestroyTexture(texture);
	SDL_Quit();

	return 0;

sdl_error:
    SDL_AndroidLogPrint(LOG_ERROR, "draw", "%s\n", SDL_GetError());
	SDL_Quit();
	//exit(EXIT_FAILURE);
	return EXIT_FAILURE;
}
