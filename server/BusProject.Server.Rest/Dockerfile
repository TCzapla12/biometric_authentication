#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["BusProject.Server.Rest/BusProject.Server.Rest.csproj", "BusProject.Server.Rest/"]
COPY ["BusProject.Server.Tests/BusProject.Server.Tests.csproj", "BusProject.Server.Tests/"]
COPY ["BusProject.Server.Logic/BusProject.Server.Logic.csproj", "BusProject.Server.Logic/"]
COPY ["BusProject.Server.Model/BusProject.Server.Model.csproj", "BusProject.Server.Model/"]
COPY ["BusProject.Server.Rest.Model/BusProject.Server.Rest.Model.csproj", "BusProject.Server.Rest.Model/"]
RUN dotnet restore "BusProject.Server.Rest/BusProject.Server.Rest.csproj"
COPY . .
WORKDIR "/src/BusProject.Server.Rest"
RUN dotnet build "BusProject.Server.Rest.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "BusProject.Server.Rest.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
COPY /Files ./Files
ENTRYPOINT ["dotnet", "BusProject.Server.Rest.dll"]