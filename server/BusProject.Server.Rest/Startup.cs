namespace BusProject.Server.Rest
{
  using Microsoft.AspNetCore.Builder;
  using Microsoft.AspNetCore.Hosting;
  using Microsoft.Extensions.Configuration;
  using Microsoft.Extensions.DependencyInjection;
  using Microsoft.Extensions.Hosting;
  using Microsoft.OpenApi.Models;

  using BusProject.Server.Model;
  using BusProject.Server.Logic;

  public class Startup
  {
    public Startup( IConfiguration configuration )
    {
      this.Configuration = configuration;
    }

    public IConfiguration Configuration { get; }

    public void ConfigureServices( IServiceCollection services )
    {
         
      services.AddControllers( );
#if DEBUG
            services.AddScoped<IFileDataBase, FileDataBase>();
#else
            services.AddScoped<IFileDataBase, FileDataBase>();
#endif

            services.AddSwaggerGen( options => { options.SwaggerDoc( "v1", new OpenApiInfo { Title = "BusProject.Server", Version = "v1" } ); } );
    }

    public void Configure( IApplicationBuilder app, IWebHostEnvironment env )
    {
      if( env.IsDevelopment( ) )
      {
       

        app.UseDeveloperExceptionPage( );

        app.UseSwagger( );

        app.UseSwaggerUI( options => options.SwaggerEndpoint( "/swagger/v1/swagger.json", "BusProject.Server v1") );
      }
      
           app.UseHttpsRedirection( );
           app.UseRouting( );
           app.UseCors();
           app.UseAuthorization( );
           app.UseEndpoints( endpoints => { endpoints.MapControllers( ); } );
    }
  }
}
